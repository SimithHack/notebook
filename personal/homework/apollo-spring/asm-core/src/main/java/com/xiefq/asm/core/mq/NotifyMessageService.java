package com.xiefq.asm.core.mq;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class NotifyMessageService {
    @Value("${mq.quota-alarm.exchange:quota_alarm}")
    private String quotaAlarmExchange;
    @Autowired
    @Qualifier("rabbitAdmin")
    private RabbitAdmin admin;

    /**
     * 声明MQ的定义
     */
    @PostConstruct
    public void initDefinition(){
        admin.declareExchange(new FanoutExchange(quotaAlarmExchange));
    }

    /**
     * 发送额度高级消息
     */
    public void notifyQuotaAlarm(){
        RabbitTemplate template = admin.getRabbitTemplate();
        template.convertAndSend("hello world");
    }
}
