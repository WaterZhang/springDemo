package com.zhangzemiao.www.springdemo.domain.feign.plugin;

public class SystemPropertyProvider {
    public String getProperty(String key)
    {
        return System.getProperty(key);
    }
}
