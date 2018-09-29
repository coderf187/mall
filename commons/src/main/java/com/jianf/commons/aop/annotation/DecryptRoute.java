package com.jianf.commons.aop.annotation;

import java.lang.annotation.*;

/**
 * 解密注解 适用于非mapper层
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DecryptRoute {
    String spelExpression() default "";
}
