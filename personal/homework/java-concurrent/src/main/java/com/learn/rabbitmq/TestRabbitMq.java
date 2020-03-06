package com.learn.rabbitmq;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collections;

public class TestRabbitMq {
    AnnotationConfigApplicationContext ctx;
    @Before
    public void before(){
        ctx = new AnnotationConfigApplicationContext();
        ctx.register(MqConfig.class);
        ctx.refresh();
        for(String name : ctx.getBeanDefinitionNames()){
            System.out.println(name);
        }
    }

    @Test
    public void test(){
        RabbitAdmin rabbitAdmin = (RabbitAdmin) ctx.getBean("rabbitAdmin");
        rabbitAdmin.declareExchange(new DirectExchange("test.direct.exchange", false, false));
        rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
        rabbitAdmin.declareBinding(new Binding(
                "test.direct.queue",
                Binding.DestinationType.QUEUE,
                "test.direct.exchange",
                "test.direct.binding",
                Collections.emptyMap()
        ));
        rabbitAdmin.getRabbitTemplate().
                send("test.direct.exchange",
                        "test.direct.binding",
                        new Message(
                                "hello world".getBytes(),
                                new MessageProperties()
                        )
                );
        rabbitAdmin.declareBinding(
                BindingBuilder
                .bind(new Queue("xxx", false))
                .to(new TopicExchange("mtopic", false, false))
                .with("user.#")
        );
    }
    @Test
    public void testSendMsg(){
        TestRabbitTemplate template = ctx.getBean(TestRabbitTemplate.class);
        template.testSendMsg();
    }
    @Test
    public void testSimpleMessageListenerContainer(){
        RabbitTemplate template = (RabbitTemplate)ctx.getBean("rabbitTemplate");
        MessageProperties properties = new MessageProperties();
        template.convertAndSend("log.exchange", "log.warning", new Message("告警消息".getBytes(), properties), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                return message;
            }
        });
        template.convertAndSend("log.exchange", "log.serious", new Message("严重消息".getBytes(), properties), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                return message;
            }
        });
        template.convertAndSend("log.exchange", "log.danger", new Message("致命消息".getBytes(), properties), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                return message;
            }
        });
    }
    @After
    public void after(){
        //ctx.close();
    }
}
