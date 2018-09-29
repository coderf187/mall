package com.jianf.commons.aop.aspect;

import com.jianf.commons.aop.annotation.Lock;
import com.jianf.commons.aop.annotation.LockObjectParameter;
import com.jianf.commons.aop.annotation.LockParameter;
import com.jianf.commons.framework.RedisService;
import org.apache.commons.lang.math.RandomUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 分布式锁
 *
 * @auth
 * @create 2017/05/23
 **/
@Aspect
@Component
public class LockAspect {
    private final Logger logger = LoggerFactory.getLogger(LockAspect.class);

    @Autowired
    private RedisService redisService;

    /**
     * 环绕通知加锁处理 
     * 
     * @param jp
     *            参数
     * @return 返回值
     * @throws Throwable
     */
    @Around(value = "@annotation(com.jianf.commons.aop.annotation.Lock)")
    public Object lock(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Object[] args = jp.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();
        Lock lock = method.getAnnotation(Lock.class);
        // 根据获取到的参数注解和参数列表获得加锁的参数
        Object lockedObject = getLockedObject(annotations, args);
        // 获取锁的前缀
        String lockPrefix = lock.lockedPrefix();

        boolean methodPrefix = lock.methodPrefix();

        RedisLock redisLock = new RedisLock(method.getName(), lockPrefix, lockedObject.toString(), methodPrefix,
                redisService);
        Object object;
        if (!lock.read()) {
            boolean lockResult = redisLock.locked(lock.timeOut(), lock.expireTime());
            // 取锁失败
            Assert.isTrue(lockResult, "获取分布式锁失败！请重试");
            try {
                // 加锁成功，执行方法
                object = jp.proceed();
            } finally {
                redisLock.unlock();// 释放锁
            }
        } else {
            boolean lockResult = redisLock.readLocked(lock.timeOut());
            // 取锁失败
            Assert.isTrue(lockResult, "获取分布式锁失败！请重试");
            object = jp.proceed();
        }
        return object;
    }

    /**
     * 判定参数注解 获取锁住的唯一标示key 
     * 
     * @param annotations
     * @param args
     * @return
     */
    private Object getLockedObject(Annotation[][] annotations, Object[] args) {

        // 不支持多个参数加锁，只支持第一个注解为LockObjectParameter的参数
        // 标记参数的位置指针
        int index = -1;
        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                    // LockObjectParameter
                if (annotations[i][j] instanceof LockObjectParameter) {
                    index = i;
                    try {
                        Class<? extends Object> targetClass = args[i].getClass();
                        Field field = targetClass.getDeclaredField(((LockObjectParameter) annotations[i][j]).field());
                        field.setAccessible(true);
                        return field.get(args[i]);

                    } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
                        throw new RuntimeException("注解对象中没有该属性" + ((LockObjectParameter) annotations[i][j]).field());
                    }
                }

                if (annotations[i][j] instanceof LockParameter) {
                    index = i;
                    break;
                }
            }
            // 找到第一个后直接break，不支持多参数加锁
            if (index != -1) {
                break;
            }
        }

        if (index == -1) {
            throw new RuntimeException("请指定被锁定参数");
        }

        return args[index];
    }

    /**
     * 与redis交互的锁操作
     */
    class RedisLock {
        private final Logger logger = LoggerFactory.getLogger(RedisLock.class);

        /**
         * 纳秒和毫秒之间的转换率
         */
        public static final long MILLI_NANO_TIME = 1000 * 1000L;

        public static final String LOCKED = "TRUE";

        private boolean lock = true;

        private RedisService redisService;

        private String key;

        /**
         * 生成锁key规则 方法+前缀+lockObject
         * @param methodName
         * @param lockPrefix
         * @param lockedObject
         * @param methodPrefix
         * @param client
         */
        public RedisLock(String methodName, String lockPrefix, String lockedObject, boolean methodPrefix,
                RedisService client) {
            if (methodPrefix) {
                this.key = methodName + "_" + lockPrefix + "_" + lockedObject;
            } else {
                this.key = lockPrefix + "_" + lockedObject;
            }
            this.redisService = client;
        }

        /**
         * 加锁 
         * 
         * @param timeout
         *            timeout的时间范围内轮询锁
         * @param expire
         *            设置锁超时时间
         * @return
         */
        public boolean locked(long timeout, int expire) {
            long nanoTime = System.nanoTime();
            timeout *= MILLI_NANO_TIME;
            try {
                // 在timeout的时间范围内不断轮询锁
                while (System.nanoTime() - nanoTime < timeout) {
                    // 锁不存在的话，设置锁并设置锁过期时间，即加锁
                    if (this.redisService.setnx(this.key, LOCKED)) {
                        // 设置锁过期时间是为了在没有释放
                        this.redisService.expire(this.key, expire);
                        // 锁的情况下锁过期后消失，不会造成永久阻塞
                        this.lock = true;
                        return this.lock;
                    }
                    // 短暂休眠，避免可能的活锁
                    Thread.sleep(3, RandomUtils.nextInt(30));
                }
            } catch (Exception e) {
                throw new RuntimeException("locking error", e);
            }
            return false;
        }

        /**
         * 读取锁，适用场景：防止并发读写同一数据时发生不一致现象,插入时设置分布式锁,并在读取时设置读锁,如果同一时间又插入又查询,
         * 查询会阻塞等待分布式锁释放后在进行查询操作。
         * 
         * 
         * @param timeout
         * @return
         */
        public boolean readLocked(long timeout) {
            long nanoTime = System.nanoTime();
            timeout *= MILLI_NANO_TIME;
            try {
                // 在timeout的时间范围内不断轮询锁
                while (System.nanoTime() - nanoTime < timeout) {
                    // 读取锁,并不会对该操作进行加锁处理,但是会查询指定key是否存在并阻塞,锁释放后执行
                    if (!Objects.equals(this.redisService.get(this.key), LOCKED)) {
                        this.lock = true;
                        return this.lock;
                    }
                    // 短暂休眠，避免可能的活锁
                    Thread.sleep(3, RandomUtils.nextInt(30));
                }
            } catch (Exception e) {
                throw new RuntimeException("locking error", e);
            }
            return false;
        }

        /**
         * 删除分布式锁id
         */
        public void unlock() {
            try {
                if (this.lock) {
                    // 直接删除
                    redisService.del(this.key);
                }
            } catch (Exception e) {
                logger.error("删除分布式锁唯一key失败,失败原因{}", e);
            }
        }
    }

}
