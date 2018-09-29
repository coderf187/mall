package com.jianf.commons.aop.annotation;

import java.lang.annotation.*;

/**
 * 返回值加密注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ReturnEncrypt {
    String spelExpression() default "";
}
