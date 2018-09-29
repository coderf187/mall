package com.jianf.commons.utils;

import org.springframework.context.ApplicationContext;

/**
 * 获取spring上下文容器 Created by fengjian on 2016/12/16.
 */
public class SpringContextUtil {

    private static ApplicationContext applicationContext;

    private SpringContextUtil() {
    }


    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }
}
