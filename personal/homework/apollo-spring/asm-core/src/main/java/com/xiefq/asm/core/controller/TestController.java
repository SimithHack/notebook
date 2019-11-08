package com.xiefq.asm.core.controller;

import com.xiefq.asm.core.mq.NotifyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试http接口
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private NotifyMessageService messageService;

    @GetMapping
    public ResponseEntity test(){
        messageService.notifyQuotaAlarm();
        return ResponseEntity.ok("");
    }
}
