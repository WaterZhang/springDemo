package com.zhangzemiao.www.springdemo.domain.feign.log;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.HystrixInvokableInfo;
import com.netflix.hystrix.HystrixObservableCommand;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolMetrics;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.zhangzemiao.www.springdemo.domain.feign.exception.FeignApplicationException;
import com.zhangzemiao.www.springdemo.domain.feign.plugin.TransactionAttributesOverrides;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.MDC;

public class HystrixLogger {

    static final String EXECUTED_STATE = "executed";
    private Logger transactionLogger;
    private Logger errorLogger;
    private TransactionAttributesOverrides transactionAttributesOverrides;
    private MDCListener mdcListener;

    public HystrixLogger(LoggerFactoryProvider loggerFactoryProvider) {
        this(loggerFactoryProvider, null);
    }

    public HystrixLogger(LoggerFactoryProvider loggerFactoryProvider, TransactionAttributesOverrides transactionAttributesOverrides) {
        this(loggerFactoryProvider, transactionAttributesOverrides, null);
    }

    public HystrixLogger(LoggerFactoryProvider loggerFactoryProvider, TransactionAttributesOverrides transactionAttributesOverrides, MDCListener mdcListener) {
        this.transactionLogger = loggerFactoryProvider.getLogger(LoggerConstants.TRANSACTION_LOGGER_NAME);
        this.errorLogger = loggerFactoryProvider.getLogger(LoggerConstants.ERROR_LOGGER_NAME);
        this.transactionAttributesOverrides = transactionAttributesOverrides;
        this.mdcListener = mdcListener;
    }

    public <T> void logTransaction(HystrixInvokableInfo<T> commandInstance) {
        logTransaction(commandInstance, new LinkedHashMap<>());
    }

    public <T> void logTransaction(HystrixInvokableInfo<T> commandInstance, Map<String, String> eventMap) {
        if (eventMap == null)
            eventMap = new LinkedHashMap<>();

        populateTransactionFields(commandInstance, eventMap, null);
        if (transactionAttributesOverrides != null) {
            transactionAttributesOverrides.apply(commandInstance, eventMap);
        }
        final String eventLog = constructEventLog(eventMap, ", ");
        transactionLogger.info(String.format("%s, message=[Transaction logging message]", eventLog));
        notifyMdcListenerOnTransaction();
    }

    public <T> void logError(HystrixInvokableInfo<T> commandInstance, Exception e) {
        logError(commandInstance, e, new LinkedHashMap<>());
    }

    public <T> void logError(HystrixInvokableInfo<T> commandInstance, Throwable e, Map<String, String> eventMap) {
        if (eventMap == null)
            eventMap = new HashMap<>();

        populateTransactionFields(commandInstance, eventMap, e);
        printErrorMessage(e, eventMap);
        notifyMdcListenerOnError();
    }

    public <T> void logError(HystrixInvokable<T> hystrixInvokable, HystrixRuntimeException.FailureType failureType, Throwable e) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("failureType", failureType.name());
        populateTransactionFields(hystrixInvokable, params, e);
        printErrorMessage(e, params);
        notifyMdcListenerOnError();
    }

    private void printErrorMessage(Throwable e, Map<String, String> eventMap) {
        String message = String.format("%s message=[Transaction Error logging]", Utils.asSplunkLogString(eventMap));
        if (e instanceof HystrixBadRequestException) {
            errorLogger.warn(message);
        } else {
            errorLogger.error(message);
        }
    }

    private static void populateTransactionFields(HystrixInvokable invokable, Map<String, String> eventMap, Throwable e) {
        if (invokable instanceof HystrixInvokableInfo) {
            populateTransactionFields((HystrixInvokableInfo) invokable, eventMap, e);
        }
    }

    private static void populateTransactionFields(HystrixInvokableInfo invokableInfo, Map<String, String> eventMap, Throwable e) {

        eventMap.put(LoggerConstants.EVENT_NAME, constructEventName(invokableInfo));
        eventMap.put(LoggerConstants.GROUP_NAME, constructGroupName(invokableInfo));

        Throwable exception = getException(e, invokableInfo);
        eventMap.put(LoggerConstants.SUCCESS, isExecutionSuccessful(invokableInfo, exception));

        // add failure reason if present. Special handling for timeouts, as failedExecutionException is not set by Hystrix in case of timeouts
        if (invokableInfo.isResponseTimedOut()) {
            eventMap.put(LoggerConstants.FAILURE_REASON, "timed-out");
        } else if (exception != null) {
            eventMap.put(LoggerConstants.FAILURE_REASON, Utils.asSplunkMessage(String.format("Exception: %s", exception.getMessage())));
        }

        eventMap.put(LoggerConstants.THREAD_TIMEOUT, Integer.toString(invokableInfo.getProperties().executionTimeoutInMilliseconds().get()));
        eventMap.put(LoggerConstants.FALLBACK, Boolean.toString(invokableInfo.isResponseFromFallback()));

        eventMap.put(LoggerConstants.CIRCUIT_OPEN, Boolean.toString(invokableInfo.isCircuitBreakerOpen()));
        eventMap.put(LoggerConstants.THREAD_POOL_REJECTED, Boolean.toString(invokableInfo.isResponseRejected()));

        HystrixThreadPoolKey threadPoolKey = invokableInfo.getThreadPoolKey();
        if (threadPoolKey != null) {
            HystrixThreadPoolMetrics threadPoolMetrics = HystrixThreadPoolMetrics.getInstance(threadPoolKey);
            if (threadPoolMetrics != null) {
                eventMap.put(LoggerConstants.THREAD_POOL_SIZE, Objects.toString(threadPoolMetrics.getProperties().coreSize().get()));
                eventMap.put(LoggerConstants.CURRENT_THREAD_POOL_SIZE, Objects.toString(threadPoolMetrics.getCurrentCorePoolSize()));
                eventMap.put(LoggerConstants.NUMBER_OF_ACTIVE_THREADS, Objects.toString(threadPoolMetrics.getCurrentActiveCount()));
            }
        }
        eventMap.put(LoggerConstants.DURATION, Integer.toString(invokableInfo.getExecutionTimeInMilliseconds()));
    }

    private static Throwable getException(Throwable e, HystrixInvokableInfo invokableInfo) {
        Throwable throwable = null;
        if (e != null) {
            throwable = e;
        } else if (invokableInfo instanceof HystrixCommand) {
            throwable = ((HystrixCommand) invokableInfo).getExecutionException();
        } else if (invokableInfo instanceof HystrixObservableCommand) {
            throwable = ((HystrixObservableCommand) invokableInfo).getExecutionException();
        }
        return throwable;
    }

    private String constructEventLog(Map<String, String> eventMap, String separator) {
        final List<String> keyValuePairs = new ArrayList<>();
        for (String key : eventMap.keySet()) {
            keyValuePairs.add(key + "=" + eventMap.get(key));
        }
        return StringUtils.join(keyValuePairs, separator);
    }

    private static <T> String constructEventName(HystrixInvokableInfo<T> commandInstance) {
        return commandInstance.getCommandKey().name();
    }

    private static String constructGroupName(HystrixInvokableInfo invokableInfo) {
        return invokableInfo.getCommandGroup() == null ? "null" : invokableInfo.getCommandGroup().name();
    }

    private static String isExecutionSuccessful(HystrixInvokableInfo invokableInfo, Throwable e) {
        return (invokableInfo.getExecutionEvents().size() <= 0)
               ? EXECUTED_STATE
               : Boolean.toString(invokableInfo.isSuccessfulExecution() || e instanceof FeignApplicationException);
    }


    private void notifyMdcListenerOnTransaction() {
        if (mdcListener != null) {
            mdcListener.onTransaction(MDC.getCopyOfContextMap());
        }
    }

    private void notifyMdcListenerOnError() {
        if (mdcListener != null) {
            mdcListener.onError(MDC.getCopyOfContextMap());
        }
    }

    public interface MDCListener {
        void onError(Map<String, String> mdc);
        void onTransaction(Map<String, String> mdc);
    }

}
