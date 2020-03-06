package com.xiefq.learn.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.ParentContextApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * spring boot初始化，可以自定以spring-boot启动完成之后的动作
 */
@Slf4j
public class MyInitializer extends ParentContextApplicationContextInitializer {

    public MyInitializer(ApplicationContext parent) {
        super(parent);
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        log.info("spring-boot 初始化完成");
    }
}
