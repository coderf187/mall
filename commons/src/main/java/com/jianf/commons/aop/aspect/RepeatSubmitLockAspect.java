package com.jianf.commons.aop.aspect;

import com.jianf.commons.aop.annotation.RepeatSubmitLock;
import com.jianf.commons.dto.base.BaseHeaderVo;
import com.jianf.commons.exception.BusinessException;
import com.jianf.commons.framework.RedisService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 指定时间内重复请求检查
 *
 * @auth fengjian
 * @create 2018/6/4
 **/
@Aspect
@Component
public class RepeatSubmitLockAspect {
    private final Logger LOGGER = LoggerFactory.getLogger(RepeatSubmitLockAspect.class);
    /**
     * 字符串链接符号
     */
    private final static String CONTACT_STR = "_";
    /**
     * redis key 前缀
     */
    private final static String KEY_PREFIX = "jianf_submit";
    @Autowired
    private RedisService redisService;

    @Pointcut("@annotation(com.jianf.commons.aop.annotation.RepeatSubmitLock)")
    private void cut() {
    }

    /**
     * 被拦截方法before进行 切面处理保存请求参数日志,便于排查问题
     */
    @Order(Integer.MAX_VALUE)
    @Before("cut()")
    public void checkRepeatBeforeMethod(JoinPoint jp) {
        // 获取切面的签名
        MethodSignature signature = (MethodSignature) jp.getSignature();
        // 获取被拦截的方法
        Method method = signature.getMethod();

        RepeatSubmitLock lock = method.getDeclaredAnnotation(RepeatSubmitLock.class);

        String repeatSubmitLogRedisKey = getRepeatSubmitLogRedisKey(jp);

        // 查询redis是不是有这个key
        String result = redisService.get(repeatSubmitLogRedisKey);
        // 在指定时间内存在这个key就报错
        if (null != result) {
            LOGGER.error("用户在上次请求时间：" + result + "请求最短间隔：" + lock.expireTime());
            LOGGER.error("请求redis key：" + repeatSubmitLogRedisKey);
            throw BusinessException.ofErrorRepeat();
        } else {
            // 不存在就在redis里面存放一份
            redisService.setString(repeatSubmitLogRedisKey, System.currentTimeMillis() + "");

            redisService.expire(repeatSubmitLogRedisKey, lock.expireTime());
        }
    }

    /**
     *
     * 被拦截方法after后进行 切面处理删除20秒不能重复提交的锁
     */
    @Order(Integer.MAX_VALUE)
    @After(value = "cut()")
    public void logAfterMethod(JoinPoint jp) {
        String queryKey = getRepeatSubmitLogRedisKey(jp);

        LOGGER.info("用户在上次请求完：{}，删除锁", queryKey);
        redisService.del(queryKey);
    }

    private String getRepeatSubmitLogRedisKey(JoinPoint jp) {
        // 获取切面的签名
        MethodSignature signature = (MethodSignature) jp.getSignature();
        // 获取被拦截的方法
        Method method = signature.getMethod();
        // 获取注解
        RepeatSubmitLock lock = method.getDeclaredAnnotation(RepeatSubmitLock.class);
        // 获取类名称
        String className = signature.getDeclaringTypeName();
        className = className.substring(className.lastIndexOf(".") + 1);
        // 获取方法名称
        String methodName = method.getName();
        // 获取参数
        Object[] args = jp.getArgs();
        // 请求参数错误
        if (null == args) {
            throw BusinessException.ofErrorParameter();
        }

        BaseHeaderVo baseHeaderVo;
        try {
            baseHeaderVo = (BaseHeaderVo) args[0];
        } catch (Exception e) {
            throw BusinessException.ofErrorParameter();
        }
        List<String> params = new ArrayList<String>();
        params.add(KEY_PREFIX);
        params.add(className);
        params.add(methodName);
        params.add(baseHeaderVo.getUid().toString());
        return getRedisKey(params);
    }

    /**
     * 获取redis的键：
     * prefix +
     * className +
     * methodName +
     * uid +
     *
     * @param params
     */
    public String getRedisKey(List<String> params) {
        StringBuffer keyBuf = new StringBuffer();
        params.forEach(param -> keyBuf.append(param).append(CONTACT_STR));
        return keyBuf.deleteCharAt(keyBuf.lastIndexOf("_")).toString();
    }


}
