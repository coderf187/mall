package com.jianf.commons.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: fengjian
 * @ClassName: RedisProperties
 * @Description: 获取properties redis属性
 * @date 2016年5月7日 下午10:02:05
 */
@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisProperties {

    List<String> nodes;

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

}
