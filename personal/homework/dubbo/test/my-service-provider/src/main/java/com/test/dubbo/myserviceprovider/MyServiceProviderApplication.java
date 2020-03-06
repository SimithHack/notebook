package com.test.dubbo.myserviceprovider;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@DubboComponentScan("com.test.dubbo.myserviceprovider.dubbo")
public class MyServiceProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyServiceProviderApplication.class, args);
	}

}
