package com.xiefq.learn.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * bean的生命周期管理
 */
@Service
@Slf4j
public class BeanLifeCycleService {
    public BeanLifeCycleService(){
        log.info("构造方法调用");
    }
    @PostConstruct
    public void initMethod(){
        log.info("构造方法之后调用");
    }
    @PreDestroy
    public void destoryMethod(){
        log.info("销毁前调用");
    }
}
