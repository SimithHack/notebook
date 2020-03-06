package com.xiefq.asm.core;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 风控中心 启动类
 */
@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.juma.risk.core.service.rpcimpl")
public class AsmCoreApplication {
    public static void main(String[] args){
        SpringApplication.run(AsmCoreApplication.class, args);
    }
}
