package com.zhangzemiao.www.springdemo.domain.feign;

import com.zhangzemiao.www.springdemo.domain.feign.log.HystrixInitializer;
import com.zhangzemiao.www.springdemo.domain.feign.log.HystrixLogger;
import com.zhangzemiao.www.springdemo.domain.feign.log.LoggerFactoryProvider;
import com.zhangzemiao.www.springdemo.domain.feign.plugin.ICallableWrapper;
import com.zhangzemiao.www.springdemo.domain.feign.plugin.TransactionAttributesOverrides;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class HystrixInitializingInterceptor extends HandlerInterceptorAdapter {
    private final HystrixInitializer hystrixInitializer;

    public HystrixInitializingInterceptor(boolean isMDCAware,
                                          boolean allowCoreThreadTimeOut,
                                          Map<String, String> configuration){
        this(isMDCAware, allowCoreThreadTimeOut, configuration, null);
    }

    public HystrixInitializingInterceptor(boolean isMDCAware,
                                          boolean allowCoreThreadTimeOut,
                                          Map<String, String> configuration,
                                          TransactionAttributesOverrides transactionAttributesOverrides) {
        this(isMDCAware, allowCoreThreadTimeOut, configuration, true,
             new HystrixLogger(new LoggerFactoryProvider(), transactionAttributesOverrides), null);
    }

    HystrixInitializingInterceptor(boolean isMDCAware, boolean allowCoreThreadTimeOut,
                                   Map<String, String> configuration,
                                   boolean useFeign8CompatibleStrategy, HystrixLogger connectorLogger,
                                   List<ICallableWrapper> customCallableWrapperList) {
        this.hystrixInitializer = new HystrixInitializer.Builder(configuration)
                                      .withIsMDCAware(isMDCAware).withAllowCoreThreadTimeOut(allowCoreThreadTimeOut)
                                      .withUseFeign8CompatibleStrategy(useFeign8CompatibleStrategy)
                                      .withHystrixLogger(connectorLogger)
                                      .withCallableWrapperList(customCallableWrapperList)
                                      .build();

        this.hystrixInitializer.init();
    }

}
