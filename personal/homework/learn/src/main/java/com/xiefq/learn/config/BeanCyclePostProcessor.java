package com.xiefq.learn.config;


import com.xiefq.learn.service.BeanLifeCycleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BeanCyclePostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean.getClass().equals(BeanLifeCycleService.class)){
            log.info("初始化开始前");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean.getClass().equals(BeanLifeCycleService.class)){
            log.info("初始化结束后");
        }
        return bean;
    }

}
