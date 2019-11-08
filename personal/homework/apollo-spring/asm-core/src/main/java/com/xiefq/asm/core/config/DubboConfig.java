package com.xiefq.asm.core.config;

import org.apache.dubbo.config.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * dubbo配置
 */
@Configuration
public class DubboConfig {

    @Value("${spring.application.name}")
    private String name;
    @Value("${dubbo.zookeeper.address}")
    private String registryZkAddress;
    @Value("${dubbo.port:12345}")
    private Integer dubboPort;

    /**
     * 应用配置
     * @return
     */
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(name);
        return applicationConfig;
    }

    /**
     * 服务注册中心配置
     * @return
     */
    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(registryZkAddress);
        registryConfig.setClient("curator");
        return registryConfig;
    }

    /**
     * 配置中心
     * @return
     */
    @Bean
    public ConfigCenterConfig configCenterConfig(){
        ConfigCenterConfig cfg = new ConfigCenterConfig();
        cfg.setAddress(registryZkAddress);
        return cfg;
    }

    /**
     * 元数据配置
     * @return
     */
    @Bean
    public MetadataReportConfig metadataReportConfig(){
        MetadataReportConfig cfg = new MetadataReportConfig();
        cfg.setAddress(registryZkAddress);
        return cfg;
    }

    /**
     * 消费
     * @return
     */
    @Bean
    public ConsumerConfig consumerConfig(){
        ConsumerConfig cfg = new ConsumerConfig();
        return cfg;
    }

    /**
     * 服务提供者
     * @return
     */
    @Bean
    public ProviderConfig providerConfig(){
        ProviderConfig cfg = new ProviderConfig();
        return cfg;
    }
    /**
     * 注册协议方面配置
     * @return
     */
    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(dubboPort);
        protocolConfig.setSerialization("gson");
        return protocolConfig;
    }
}
