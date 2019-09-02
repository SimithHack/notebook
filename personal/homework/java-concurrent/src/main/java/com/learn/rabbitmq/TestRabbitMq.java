package com.learn.rabbitmq;

import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collections;

public class TestRabbitMq {

    @Test
    public void test(){
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(MqConfig.class);
        ctx.refresh();
        for(String name : ctx.getBeanDefinitionNames()){
            System.out.println(name);
        }
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
}
