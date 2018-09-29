package com.jianf.commons.aop.annotation;


import java.lang.annotation.*;

/**
 * 参数注解 对象的注解
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockObjectParameter {
    //具体字段
    String field() default "";
}
