package com.jianf.commons.aop.annotation;

import java.lang.annotation.*;

/**
 * 方法结束后加日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AfterAspectLog {
     String collectionName();
}
