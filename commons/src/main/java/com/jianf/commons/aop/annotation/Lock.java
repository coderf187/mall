package com.jianf.commons.aop.annotation;

import java.lang.annotation.*;

/**
 * 分布式锁注解 方法注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {
    String lockedPrefix() default "";// redis 锁key的前缀

    long timeOut() default 3000;// 轮询锁的时间

    int expireTime() default 1000;// key在redis里存在的时间，1000S

    boolean methodPrefix() default true;//是否使用方法名作为前缀一部分

    boolean read() default false;//使用读取锁,设置为true 不会加锁，会阻塞设置的key的操作,待key释放后再执行
}
