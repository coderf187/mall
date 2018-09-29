package com.jianf.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author: fengjian
 * @ClassName: RestTemplateConfig
 * @Description: RestTemplate配置
 * @date 2016年5月7日 下午10:02:29
 */
@Configuration
public class RestTemplateConfig {

    /**
     * @auth:fengjian
     * @Title: configRestTemplate
     * @Description: 初始化resttemplate LoadBalanced用ribbon 做负载均衡
     * @param @return 设定文件
     * @return RestTemplate 返回类型
     * @throws
     */
    @Bean(name = "restTemplate1")
    public RestTemplate configRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setReadTimeout(30000);
        httpComponentsClientHttpRequestFactory.setConnectTimeout(30000);
        RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
        // 设置errorHandler，返回结果到业务层，业务层去处理
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
            }
        });
        return restTemplate;
    }

    /**
     * @auth:fengjian
     * @Title: asyncRestTemplate
     * @Description: 异步resttemplate
     * @param @return 设定文件
     * @return AsyncRestTemplate 返回类型
     * @throws
     */
    @Bean(name = "asyncRestTemplate")
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate();
    }

}
