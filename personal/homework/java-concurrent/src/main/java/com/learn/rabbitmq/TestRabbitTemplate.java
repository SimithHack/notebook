package com.learn.rabbitmq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestRabbitTemplate {
    @Autowired
    private RabbitTemplate template;
    @Autowired
    private ConnectionFactory conn;

    public void testSendMsg(){
        MessageProperties properties = new MessageProperties();
        properties.getHeaders().put("desc", "信息描述");
        Message msg = new Message("Hello World".getBytes(), properties);
        template.convertAndSend("test.direct.exchange", "test.direct.binding", msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("发送消息之前，可以对消息进行修改");
                message.getMessageProperties().getHeaders().put("info", "一些描述信息");
                return message;
            }
        });
    }

    /**
     * 测试MessageListenerAdapter
     */
    public void testMessageListenerAdapter(){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(conn);
        //底层，默认的监听方法是 handleMessage 方法，在委托类MessageDelegate必须提供名称相同的，来绑定
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        //可以通过这种方法来改默认方法的名称
        adapter.setDefaultListenerMethod("myName");
        //可以设置MessageConverter来定义，参数类型的行为，实现下面的方法就可以
        adapter.setMessageConverter(new MessageConverter() {
            //把Java对象转换为Message
            @Override
            public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
                return new Message(object.toString().getBytes(), messageProperties);
            }
            //把Message对象转换为我们的Java对象，这个返回的是什么类型的对象，就调用什么类型的参数方法
            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                return message.getBody();
            }
        });
        //还可以指定队列和方法名绑定
        Map<String, String> queueToMethodName = new HashMap<>();
        queueToMethodName.put("queue001", "method1");
        queueToMethodName.put("queue002", "method2");
        queueToMethodName.put("queue003", "method3");
        adapter.setQueueOrTagToMethodName(queueToMethodName);

        container.setMessageListener(adapter);
    }

}
