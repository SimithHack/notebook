package com.xiefq.learn;

import com.xiefq.learn.springboot.MyInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LearnApplication {


	public static void main(String[] args) {
		SpringApplication app = new SpringApplication();
		app.run(args);
	}

}
