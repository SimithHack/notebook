package com.xiefq.asm.core.config;

import com.xiefq.asm.core.config.RabbitMqConfig;
import lombok.Data;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

/**
 * 对接FMS系统 MQ配置
 */
@Configuration
public class FmsRabbitMqConfig implements RabbitListenerConfigurer {

    @Autowired
    private RabbitMqConfig.MqProperties mqProperties;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    /**
     * FMS mq配置
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "mq.fms")
    public FmsMqProperties fmsProperties(){
        return new FmsMqProperties();
    }



    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(new MappingJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }

    /**
     * FMS MQ配置
     */
    @Data
    class FmsMqProperties {
        private String virtualHost;
        private String exchange;
        private String routingKey;
        private String queue;
    }

    /**
     * MQ连接工厂
     * @return
     */
    @Bean
    public ConnectionFactory fmsConnectionFactory(){
        FmsMqProperties fms = fmsProperties();
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(mqProperties.getAddresses());
        connectionFactory.setUsername(mqProperties.getUsername());
        connectionFactory.setPassword(mqProperties.getPassword());
        connectionFactory.setVirtualHost(fms.getVirtualHost());
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
        return connectionFactory;
    }

    /**
     * rabbitmq admin
     * @return
     */
    @Bean
    public RabbitAdmin fmsRabbitAdmin(){
        RabbitAdmin admin = new RabbitAdmin(fmsConnectionFactory());
        admin.setAutoStartup(true);
        return admin;
    }

    /**
     * RabbitTemplate
     * @return
     */
    @Bean
    public RabbitTemplate fmsMqTemplate(){
        return fmsRabbitAdmin().getRabbitTemplate();
    }

    /**
     * 消息监听容器
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory fmsListenerContainerFactory(){
        SimpleRabbitListenerContainerFactory sf = new SimpleRabbitListenerContainerFactory();
        sf.setConnectionFactory(fmsConnectionFactory());
        sf.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return sf;
    }
}
