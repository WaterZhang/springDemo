package com.zhangzemiao.www.springdemo.domain.feign.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerFactoryProvider {

    public Logger getLogger(String name) {
        return LoggerFactory.getLogger(name);
    }

    public Logger getLogger(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
