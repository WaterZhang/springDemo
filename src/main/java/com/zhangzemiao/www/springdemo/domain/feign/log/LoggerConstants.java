package com.zhangzemiao.www.springdemo.domain.feign.log;

@SuppressWarnings("PMD")
public interface LoggerConstants {
        String FALLBACK = "fallback";
        String CIRCUIT_OPEN = "circuitOpen";
        String THREAD_POOL_REJECTED = "threadPoolRejected";
        String THREAD_POOL_SIZE = "threadPoolSize";
        String CURRENT_THREAD_POOL_SIZE = "currentThreadPoolSize";
        String NUMBER_OF_ACTIVE_THREADS = "numberOfActiveThreads";
        String THREAD_TIMEOUT = "threadTimeout";
        String DURATION = "duration";
        String CONNECT_DURATION = "connectDuration";
        String READ_DURATION = "readDuration";
        String EVENT_NAME = "eventName";
        String GROUP_NAME = "groupName";
        String LOG_LEVEL = "logLevel";
        String HTTP_METHOD = "method";
        String HTTP_URL = "url";
        String HTTP_REASON = "reason";
        String HTTP_STATUS = "status";
        String FAILURE_REASON = "failureReason";
        String SUCCESS = "success";
        String REQUEST = "request";
        String RESPONSE = "response";
        String BODY_LENGTH = "body-length";
        String HEADER_PREFIX = "header.";

        // these constants(*_LOGGER_NAME) should not be changed for compatibility reasons
        String TRACE_LOGGER_NAME = FeignLogger.class.getName() + ".Trace";
        String TRANSACTION_LOGGER_NAME = FeignLogger.class.getName() + ".Transaction";
        String ERROR_LOGGER_NAME = "com.zhangzemiao.www.springdemo.domain.feign.log.HystrixErrorLogger.Error";

}
