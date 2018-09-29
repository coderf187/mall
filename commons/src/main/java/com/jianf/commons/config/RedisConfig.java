package com.jianf.commons.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;

/**
 * @author: fengjian
 * @ClassName: RedisConfig
 * @Description: redis配置
 * @date 2016年5月7日 下午10:01:52
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisConfig {

    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    private String nodes;

    @Value("${spring.redis.password}")
    private String passWord;

    @Value("${spring.redis.cluster.max-redirects}")
    private int maxRedirects;


    /**
     * @auth:fengjian @Title: redisConnectionFactory @Description:
     *                获取redis连接 @param @return 设定文件 @return
     *                JedisConnectionFactory 返回类型 @throws
     */
    @Bean
    public JedisConnectionFactory  connectionFactory() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(Arrays.asList(nodes.split(",")));
        redisClusterConfiguration.setMaxRedirects(maxRedirects);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxIdle(maxIdle);
        JedisConnectionFactory factory = new JedisConnectionFactory(redisClusterConfiguration,jedisPoolConfig);
        factory.setPassword(passWord);
        return factory;
    }







    /**
     * @auth:fengjian @Title: redisTemplate @Description:
     *                初始化redisTemplate @param @param factory @param @return
     *                设定文件 @return RedisTemplate<String,String> 返回类型 @throws
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        setSerializer(template);
        template.afterPropertiesSet();
        return template;
    }

    private void setSerializer(StringRedisTemplate template) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
    }

    public String getNodes() {
        return nodes;
    }

    public RedisConfig setNodes(String nodes) {
        this.nodes = nodes;
        return this;
    }
}
