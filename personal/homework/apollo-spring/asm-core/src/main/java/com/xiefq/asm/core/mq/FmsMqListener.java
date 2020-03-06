package com.xiefq.asm.core.mq;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * fms系统消息监听
 */
@Service
@Slf4j
public class FmsMqListener {
    @RabbitListener(
            admin = "fmsRabbitAdmin",
            containerFactory = "fmsListenerContainerFactory",
            bindings = {
                    @QueueBinding(
                            value = @Queue(
                                    name="${mq.fms.queue}",
                                    durable = "true"
                            ),
                            exchange = @Exchange(
                                    name="${mq.fms.exchange}",
                                    type= ExchangeTypes.FANOUT
                            ),
                            key = "${mq.fms.routing-key}"
                    )
            }
    )
    public void onMessage(Message msg, @Payload Map record){
        //TODO: fms回调，需要更新额度信息
        log.info("incoming msg from fms {}", new Gson().toJson(record));
    }
}
