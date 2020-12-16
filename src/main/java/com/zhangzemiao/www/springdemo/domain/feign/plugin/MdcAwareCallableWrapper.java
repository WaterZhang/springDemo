package com.zhangzemiao.www.springdemo.domain.feign.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.slf4j.MDC;

public class MdcAwareCallableWrapper implements ICallableWrapper {

    @Override
    public <T> Callable<T> wrapCallable(final Callable<T> callable) {
        return new MdcAwareCallable<>(callable, MDC.getCopyOfContextMap());
    }

    private static class MdcAwareCallable<T> implements Callable<T> {

        private final Callable<T> callable;

        private final Map<String, String> callerThreadContextMap;

        public MdcAwareCallable(final Callable<T> callable, final Map<String, String> callerThreadContextMap) {
            this.callable = callable;
            this.callerThreadContextMap = callerThreadContextMap != null ? callerThreadContextMap : new HashMap<>();
        }

        @Override
        public T call() {
            // capture values set on current thread
            final Map<String, String> currentThreadContextMap = MDC.getCopyOfContextMap();

            try {
                MDC.setContextMap(callerThreadContextMap);
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                MDC.clear();
                if (currentThreadContextMap != null) {
                    MDC.setContextMap(currentThreadContextMap);
                }
            }
        }
    }
}
