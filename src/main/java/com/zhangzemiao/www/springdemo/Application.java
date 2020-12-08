package com.zhangzemiao.www.springdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties
public class Application extends SpringBootServletInitializer {

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
