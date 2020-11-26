package com.zhangzemiao.www.springdemo.web;


import com.zhangzemiao.www.springdemo.Application;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(
    basePackageClasses = Application.class,
    excludeFilters = @ComponentScan.Filter({Controller.class, Configuration.class}))
public class ApplicationContextConfig {
}
