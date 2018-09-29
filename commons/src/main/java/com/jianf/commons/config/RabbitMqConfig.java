package com.jianf.commons.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by fengjian on 2018/5/30.
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * @param
     * @Author:fengjian
     * @Description:定义数据交换方式
     * @Date:下午4:08 2017/10/23
     * @return
     */
    public enum Exchange {
        /**
         *
         */
        SJ_TEST("exchange.superloan.test", "TEST");

        private String code;
        private String name;

        Exchange(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * describe: 定义routing_key
     * creat_user: fengjian
     * creat_date: 2018/5/30
     **/
    public enum RoutingKey {
        /**
         *
         */
        SJ_TEST("routingkey.superloan.test", "TEST");


        private String code;
        private String name;

        RoutingKey(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * describe: 定义队列名称
     * creat_user: fengjian
     * creat_date: 2018/5/30
     **/
    public enum Queue {
        /**
         *
         */
        SJ_TEST("queue.superloan.test", "TEST");

        private String code;
        private String name;

        Queue(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

    }

}
