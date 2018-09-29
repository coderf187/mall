package com.jianf.minishopserver;

import com.jianf.commons.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class, OAuth2AutoConfiguration.class})
@EnableCircuitBreaker
@EnableJms
@EnableRetry
@ComponentScan(basePackages = {"com.jianf"})
public class MiniShopServerApplication implements DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(MiniShopServerApplication.class);
    private static ConfigurableApplicationContext ctx;


    public static void main(String[] args) {
        ctx = SpringApplication.run(MiniShopServerApplication.class, args);
        for (String str : ctx.getEnvironment().getActiveProfiles()) {
            LOGGER.info(str);
        }
        SpringContextUtil.setApplicationContext(ctx);
        LOGGER.info("Boot Server started.");
    }

    @Override
    public void destroy() throws Exception {
        if (null != ctx && ctx.isRunning()) {
            ctx.close();
        }
    }
}
