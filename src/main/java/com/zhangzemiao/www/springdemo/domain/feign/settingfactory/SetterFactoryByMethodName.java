package com.zhangzemiao.www.springdemo.domain.feign.settingfactory;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import feign.Target;
import feign.hystrix.SetterFactory;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

public class SetterFactoryByMethodName implements SetterFactory {

    @Override
    public HystrixCommand.Setter create(Target<?> target, Method method) {
        return HystrixCommand.Setter
                   .withGroupKey(HystrixCommandGroupKey.Factory.asKey(target.name()))
                   .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(getHost(target.name())))
                   .andCommandKey(HystrixCommandKey.Factory.asKey(method.getName()));
    }

    private String getHost(final String target) {
        try {
            final org.apache.http.client.utils.URIBuilder uriBuilder =
                new org.apache.http.client.utils.URIBuilder(target).removeQuery();
            return  uriBuilder.getScheme() + "://" + uriBuilder.getHost();
        } catch (final URISyntaxException e) {
            return target;
        }
    }

}
