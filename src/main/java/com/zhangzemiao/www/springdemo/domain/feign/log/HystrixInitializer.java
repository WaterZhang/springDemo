package com.zhangzemiao.www.springdemo.domain.feign.log;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.zhangzemiao.www.springdemo.domain.feign.plugin.ICallableWrapper;
import com.zhangzemiao.www.springdemo.domain.feign.plugin.ContextAwareConcurrencyStrategy;
import com.zhangzemiao.www.springdemo.domain.feign.plugin.Feign8CompatiblePropertiesStrategy;
import com.zhangzemiao.www.springdemo.domain.feign.plugin.MdcAwareCallableWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import org.springframework.util.CollectionUtils;
import com.netflix.hystrix.contrib.servopublisher.HystrixServoMetricsPublisher;
import org.apache.commons.configuration.AbstractConfiguration;

public class HystrixInitializer {

    private final boolean isMDCAware;
    private final boolean allowCoreThreadTimeOut;
    private final Map<String, String> configuration;
    private final boolean useFeign8CompatibleStrategy;

    private HystrixLogger hystrixLogger;

    private List<ICallableWrapper> callableWrapperList = new ArrayList<>();

    private HystrixInitializer(final boolean isMDCAware,
                               final boolean allowCoreThreadTimeOut,
                               final Map<String, String> configuration,
                               final boolean useFeign8CompatibleStrategy,
                               final HystrixLogger hystrixLogger,
                               final List<ICallableWrapper> customCallableWrapperList) {
        this.isMDCAware = isMDCAware;
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
        this.configuration = configuration;
        this.useFeign8CompatibleStrategy = useFeign8CompatibleStrategy;
        this.hystrixLogger = hystrixLogger;

        addCallableWrappers(customCallableWrapperList);
    }

    private void addCallableWrappers(final List<ICallableWrapper> customCallableWrapperList) {

        if (this.isMDCAware) {
            this.callableWrapperList.add(new MdcAwareCallableWrapper());
        }
        if (!CollectionUtils.isEmpty(customCallableWrapperList)) {
            this.callableWrapperList.addAll(customCallableWrapperList);
        }
    }

    public void init() {
        HystrixPlugins.getInstance().registerCommandExecutionHook(new HystrixLoggerExecutionHook(hystrixLogger));
        HystrixPlugins.getInstance().registerMetricsPublisher(HystrixServoMetricsPublisher.getInstance());
        registerConfiguration(configuration);

        HystrixPlugins.getInstance().registerConcurrencyStrategy(new ContextAwareConcurrencyStrategy(this.callableWrapperList, this.allowCoreThreadTimeOut));

        if (useFeign8CompatibleStrategy) {
            HystrixPlugins.getInstance().registerPropertiesStrategy(new Feign8CompatiblePropertiesStrategy());
        }
    }


    private static void registerConfiguration(final Map<String, String> configuration) {
        Validate.notNull(configuration);
        final AbstractConfiguration configManager = ConfigurationManager.getConfigInstance();
        for (final Map.Entry<String, String> entry : configuration.entrySet()) {
            configManager.setProperty(entry.getKey(), entry.getValue());
        }
    }

    public static class Builder {
        private boolean isMDCAware;
        private boolean allowCoreThreadTimeOut;
        private final Map<String, String> configuration;
        private boolean useFeign8CompatibleStrategy;
        private HystrixLogger hystrixLogger;
        private List<ICallableWrapper> callableWrapperList;

        public Builder(final Map<String, String> configuration) {
            this.configuration = configuration;
        }

        public Builder withIsMDCAware(final boolean isMDCAware) {
            this.isMDCAware = isMDCAware;
            return this;
        }

        public Builder withAllowCoreThreadTimeOut(final boolean allowCoreThreadTimeOut) {
            this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
            return this;
        }

        public Builder withUseFeign8CompatibleStrategy(final boolean useFeign8CompatibleStrategy) {
            this.useFeign8CompatibleStrategy = useFeign8CompatibleStrategy;
            return this;
        }

        public Builder withHystrixLogger(final HystrixLogger hystrixLogger) {
            this.hystrixLogger = hystrixLogger;
            return this;
        }

        public Builder withCallableWrapperList(final List<ICallableWrapper> callableWrapperList) {
            this.callableWrapperList = callableWrapperList;
            return this;
        }

        public HystrixInitializer build() {
            return new HystrixInitializer(isMDCAware, allowCoreThreadTimeOut, configuration,
                                          useFeign8CompatibleStrategy, hystrixLogger, callableWrapperList);
        }
    }
}
