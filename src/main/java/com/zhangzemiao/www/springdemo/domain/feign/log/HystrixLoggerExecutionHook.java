package com.zhangzemiao.www.springdemo.domain.feign.log;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.HystrixInvokableInfo;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;

public class HystrixLoggerExecutionHook extends HystrixCommandExecutionHook {

    private HystrixLogger hystrixLogger;

    public HystrixLoggerExecutionHook(final HystrixLogger hystrixLogger) {
        super();
        this.hystrixLogger = hystrixLogger;
    }

    @Override
    public <T> T onEmit(final HystrixInvokable<T> commandInstance, final T value) {
        if (commandInstance instanceof HystrixInvokableInfo) {
            hystrixLogger.logTransaction((HystrixInvokableInfo<?>) commandInstance);
        }
        return super.onEmit(commandInstance, value);
    }

    @Override
    public <T> Exception onError(final HystrixInvokable<T> commandInstance,
                                 final HystrixRuntimeException.FailureType failureType,
                                 final Exception e) {
        hystrixLogger.logError(commandInstance, failureType, e);
        if (commandInstance instanceof HystrixInvokableInfo) {
            hystrixLogger.logTransaction((HystrixInvokableInfo<?>) commandInstance);
        }
        return super.onError(commandInstance, failureType, e);
    }

}
