package com.zhangzemiao.www.springdemo.domain.feign.plugin;

import com.netflix.hystrix.HystrixInvokableInfo;
import java.util.Map;

@FunctionalInterface
public interface TransactionAttributesOverrides {

    void apply(HystrixInvokableInfo<?> hystrixInvokableInfo, Map<String, String> attributes);

}
