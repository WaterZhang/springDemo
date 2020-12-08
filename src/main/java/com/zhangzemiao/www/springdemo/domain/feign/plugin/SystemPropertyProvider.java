package com.zhangzemiao.www.springdemo.domain.feign.plugin;

public class SystemPropertyProvider {
    public String getProperty(final String key)
    {
        return System.getProperty(key);
    }
}
