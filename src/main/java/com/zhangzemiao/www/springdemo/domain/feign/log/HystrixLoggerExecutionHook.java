package com.zhangzemiao.www.springdemo.domain.feign.log;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.HystrixInvokableInfo;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;

public class HystrixLoggerExecutionHook extends HystrixCommandExecutionHook {

    private HystrixLogger hystrixLogger;

    public HystrixLoggerExecutionHook(HystrixLogger hystrixLogger) {
        this.hystrixLogger = hystrixLogger;
    }

    @Override
    public <T> T onEmit(HystrixInvokable<T> commandInstance, T value) {
        if (commandInstance instanceof HystrixInvokableInfo) {
            hystrixLogger.logTransaction((HystrixInvokableInfo<?>) commandInstance);
        }
        return super.onEmit(commandInstance, value);
    }

    @Override
    public <T> Exception onError(HystrixInvokable<T> commandInstance, HystrixRuntimeException.FailureType failureType, Exception e) {
        hystrixLogger.logError(commandInstance, failureType, e);
        if (commandInstance instanceof HystrixInvokableInfo) {
            hystrixLogger.logTransaction((HystrixInvokableInfo<?>) commandInstance);
        }
        return super.onError(commandInstance, failureType, e);
    }

}
