package com.zhangzemiao.www.springdemo.domain.feign.log;

import java.util.Collection;
import java.util.Map;

public interface ILoggingAwareContext {

    /**
     * use {@link ILoggingAwareContext#isTraceEnabled(Map)}
     */
    boolean isTraceEnabled();

    default boolean isTraceEnabled(Map<String, Collection<String>> requestHeaders) {
        return isTraceEnabled();
    }

    Map<String, String> getContextMap();

}
