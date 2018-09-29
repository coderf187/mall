package com.jianf.commons.aop.annotation;

import java.lang.annotation.*;

/**
 * 重复提交检查
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RepeatSubmitLock {
    /**
     * 重复提交超时时间
     * 单位：秒
     * @return
     */
    long expireTime() default 10L;

}
