package com.xiefq.learn.controller.mq;

import com.xiefq.learn.service.MqListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mq")
public class MqController {
    @Autowired
    private MqListener mqListener;
    @RequestMapping("/test1")
    public ResponseEntity test1(){
        mqListener.sendMsg();
        return ResponseEntity.ok("发送中...");
    }
}
