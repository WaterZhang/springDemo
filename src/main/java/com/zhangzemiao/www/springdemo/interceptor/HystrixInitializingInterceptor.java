package com.zhangzemiao.www.springdemo.interceptor;

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

    public HystrixInitializingInterceptor(final boolean isMDCAware,
                                          final boolean allowCoreThreadTimeOut,
                                          final Map<String, String> configuration){
        this(isMDCAware, allowCoreThreadTimeOut, configuration, null);
    }

    public HystrixInitializingInterceptor(final boolean isMDCAware,
                                          final boolean allowCoreThreadTimeOut,
                                          final Map<String, String> configuration,
                                          final TransactionAttributesOverrides transactionAttributesOverrides) {
        this(isMDCAware, allowCoreThreadTimeOut, configuration, true,
             new HystrixLogger(new LoggerFactoryProvider(), transactionAttributesOverrides), null);
    }

    private HystrixInitializingInterceptor(final boolean isMDCAware,
                                           final boolean allowCoreThreadTimeOut,
                                           final Map<String, String> configuration,
                                           final boolean useFeign8CompatibleStrategy,
                                           final HystrixLogger connectorLogger,
                                           final List<ICallableWrapper> customCallableWrapperList) {
        super();
        this.hystrixInitializer = new HystrixInitializer.Builder(configuration)
                                      .withIsMDCAware(isMDCAware).withAllowCoreThreadTimeOut(allowCoreThreadTimeOut)
                                      .withUseFeign8CompatibleStrategy(useFeign8CompatibleStrategy)
                                      .withHystrixLogger(connectorLogger)
                                      .withCallableWrapperList(customCallableWrapperList)
                                      .build();

        this.hystrixInitializer.init();
    }

}
