package com.jianf.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author: fengjian
 * @ClassName: AsyncConfig
 * @Description: 异步线程配置
 * @date 2016年5月7日 下午9:58:26
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * 异步线程池
     * @return
     */
    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(25);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("mallExecutor-");
        executor.initialize();
        return executor;
    }

}
