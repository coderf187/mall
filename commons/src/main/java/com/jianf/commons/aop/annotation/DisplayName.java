package com.jianf.commons.aop.annotation;


import java.lang.annotation.*;

/**
 * 用户说明单元测试用例
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DisplayName {
    String value();
}
