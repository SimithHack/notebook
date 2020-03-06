package com.learn.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@ComponentScan("com.learn.rabbitmq")
public class MqConfig {
    @Bean
    public RabbitAdmin rabbitAdmin(){
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        admin.setAutoStartup(true);
        return admin;
    }
    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("master:5672");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
        return connectionFactory;
    }
    @Bean
    public RabbitTemplate rabbitTemplate(){
        return rabbitAdmin().getRabbitTemplate();
    }

    /**
     * 我们声明一些队列
     * @return
     */
    @Bean
    public Queue warningQueue(){
        return new Queue("warning", false, false, false);
    }
    @Bean
    public Queue seriousQueue(){
        return new Queue("serious", false, false, false);
    }
    @Bean
    public Queue dangerQueue(){
        return new Queue("danger", false, false, false);
    }
    @Bean
    public Exchange logExchange(){
        return new DirectExchange("log.exchange", false, false);
    }
    @Bean
    public Binding warningBinding(){
        return new Binding("warning", Binding.DestinationType.QUEUE, "log.exchange", "log.warning", null);
    }
    @Bean
    public Binding seriousBinding(){
        return new Binding("serious", Binding.DestinationType.QUEUE, "log.exchange", "log.serious", null);
    }
    @Bean
    public Binding dangerBinding(){
        return new Binding("danger", Binding.DestinationType.QUEUE, "log.exchange", "log.danger", null);
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer(){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        //同时可以监听多个queue
        container.addQueues(warningQueue(), seriousQueue(), dangerQueue());
        //设置同时可以有多个消费者监听，这样可以提高性能，但是影响消息的接收顺序
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        //设置是否重回队列
        container.setDefaultRequeueRejected(false);
        //设置签收模式
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //消费标签
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });
        //设置消息监听器 ChannelAwareMessageListener 有channel
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                System.out.println(String.format("消费者接收到消息%s", new String(message.getBody())));
            }
        });
        return container;
    }
}
