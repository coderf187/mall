package com.jianf.commons.aop.annotation;

import java.lang.annotation.*;

/**
 * Created by fengjian on 2017/5/23.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {

    Class<?>[] value() default {};

}
