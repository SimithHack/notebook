package com.test.dubbo.myserviceconsumer.config;

import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class DubboConfig {
    @Value("${spring.application.name}")
    private String name;
    @Bean
    public ConsumerConfig consumerConfig(){
        ConsumerConfig cfg = new ConsumerConfig();
        return cfg;
    }
    public RegistryConfig registryConfig(){
        RegistryConfig cfg = new RegistryConfig();
        cfg.setAddress("");
        cfg.setProtocol("zookeeper");
        return cfg;
    }
}
