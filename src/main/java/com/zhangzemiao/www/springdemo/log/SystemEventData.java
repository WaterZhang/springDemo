package com.zhangzemiao.www.springdemo.log;

import java.util.HashMap;
import java.util.Map;

public class SystemEventData {
    private Map<String, Object> data;

    public SystemEventData() {
        this.data = new HashMap<>();
    }

    public SystemEventData(final Map<String, Object> data) {
        this.data = data;
    }

    public SystemEventData with(final String key, final Object value) {
        data.put(key, value);
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
