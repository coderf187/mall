package com.jianf.commons.aop.annotation;

import java.lang.annotation.*;

/**
 * 解密注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ParameterDecrypt {
    String spelExpression() default "";
}
