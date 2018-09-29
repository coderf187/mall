package com.jianf.commons.aop.annotation;

import java.lang.annotation.*;

/**
 * 参数注解
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockParameter {
}
