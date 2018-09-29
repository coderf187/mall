package com.jianf.commons.aop.annotation;

import java.lang.annotation.*;

/**
 * 加密注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ParameterEncrypt {
    String spelExpression() default "";
}
