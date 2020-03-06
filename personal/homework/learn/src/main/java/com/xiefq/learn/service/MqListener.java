package com.xiefq.learn.service;

import com.rabbitmq.client.Channel;
import com.xiefq.learn.domain.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MqListener {
    @Autowired
    private RabbitTemplate template;
    @RabbitListener(
            admin = "rabbitAdmin",
            containerFactory = "listenerContainerFactory",
            bindings = {
                    @QueueBinding(
                            value = @Queue(
                                    name="test",
                                    durable = "true"
                            ),
                            exchange = @Exchange(
                                    name="test",
                                    type= ExchangeTypes.DIRECT
                            ),
                            key = "test"
                    )
            }
    )
    public void onMessage(Channel channel, Message message, @Payload Record record) throws Exception {
        log.info("收到消息={}", record.getIndex());
        Long delveryTag = (Long)message.getHeaders().get("amqp_deliveryTag");
        if(record.getIndex()==5){
            TimeUnit.SECONDS.sleep(2);
            channel.basicNack(delveryTag, false, true);
        }else{
            channel.basicAck(delveryTag, false);
        }
    }

    public void sendMsg(){
        for(int i=0; i<100; i++){
            try {
                TimeUnit.SECONDS.sleep(1);
                template.convertAndSend("test", "test", new Record(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
