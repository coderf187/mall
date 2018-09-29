package com.jianf.commons.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements ApplicationContextAware {

    /**
     * DENY：浏览器拒绝当前页面加载任何Frame页面
     * SAMEORIGIN：frame页面的地址只能为同源域名下的页面
     * ALLOW-FROM：origin为允许frame加载的页面地址
     *
     * CSRF（Cross-site request forgery）跨站请求伪造
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();
        http.csrf().disable();
    }
}