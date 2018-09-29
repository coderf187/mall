package com.jianf.commons.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

/**
 * 20180626 remove
 * Created by fengjian on 2016/10/11.
 */
//@Configuration
//@ConditionalOnProperty(name = "spring.activemq.broker-url")
public class MqConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Bean
    public ConnectionFactory getCachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        activeMQConnectionFactory.setUseAsyncSend(true);
        cachingConnectionFactory.setTargetConnectionFactory(new ActiveMQConnectionFactory(brokerUrl));
        cachingConnectionFactory.setSessionCacheSize(10);
        return cachingConnectionFactory;
    }

    @Bean
    public JmsTemplate getJmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.setPriority(9);
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setSessionAcknowledgeMode(1);
        return jmsTemplate;
    }

    @Bean(name = "ccDataInfo4sj")
    public ActiveMQQueue ccDataInfo4sj() {
        return new ActiveMQQueue("cc_data_info_sj");
    }

    @Bean(name = "ccDataInfo4sjr")
    public ActiveMQQueue ccDataInfo4sjr() {
        return new ActiveMQQueue("cc_data_info_sjr");
    }

}
