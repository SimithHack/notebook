package com.xiefq.asm.core.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

/**
 * rabbitmq配置
 */
@Configuration
@Slf4j
public class RabbitMqConfig {
    /**
     * mq配置
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.rabbitmq")
    public MqProperties mqProperties(){
        return new MqProperties();
    }


    /**
     * MQ连接工厂
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory(){
        MqProperties properties = mqProperties();
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(properties.getAddresses());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        connectionFactory.setVirtualHost(properties.getVirtualHost());
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
        return connectionFactory;
    }

    /**
     * rabbitmq admin
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(){
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        admin.setAutoStartup(true);
        return admin;
    }

    /**
     * RabbitTemplate
     * @return
     */
    @Bean
    public RabbitTemplate template(){
        return rabbitAdmin().getRabbitTemplate();
    }

    /**
     * message converter
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new MappingJackson2MessageConverter();
    }
    /**
     * mq配置
     */
    @Data
    class MqProperties {
        private String addresses;
        private String username;
        private String password;
        private String virtualHost;
    }

}
