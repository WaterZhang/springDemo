package com.zhangzemiao.www.springdemo.domain.feign.log;

import static feign.Util.UTF_8;
import static feign.Util.valuesOrEmpty;
import static java.util.Collections.emptyMap;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class FeignLoggerImpl {
    private org.slf4j.Logger traceLogger;
    private static final String BINARY_DATA = "Binary data";
    private static final String SENSITIVE_DATA_MASK = "******";

    private LogOptions logOptions;

    FeignLoggerImpl(LoggerFactoryProvider loggerFactoryProvider,
                    LogOptions logOptions) {
        this.traceLogger = loggerFactoryProvider.getLogger(LoggerConstants.TRACE_LOGGER_NAME);
        this.logOptions = logOptions;
    }

    void log(String configKey, String format, Object... args) {
        // do nothing since feign uses this method for multiple purposes
        // all trancation and trace logging should be done in logAndRebufferResponse and logRequest methods
    }

    void logRequest(String configKey, Logger.Level logLevel, Request request) {
        if (logOptions.isEnabled(getRequestHeaders(request))) {
            Map<String, String> requestParams = new LinkedHashMap<>();
            populateLoggerContextAttributes(requestParams);
            requestParams.put(LoggerConstants.LOG_LEVEL, logLevel.name());
            requestParams.put(LoggerConstants.EVENT_NAME, constructEventName(configKey));
            requestParams.put(LoggerConstants.HTTP_METHOD, request.method());
            requestParams.put(LoggerConstants.HTTP_URL, request.url());

            int bodyLength = 0;
            if (request.body() != null) {
                bodyLength = request.body().length;
                String body = Utils.decodeOrDefault(request.body(), request.charset(), BINARY_DATA);
                requestParams.put(LoggerConstants.REQUEST, Utils.asSplunkMessage(body));
            }
            requestParams.put(LoggerConstants.BODY_LENGTH, Long.toString(bodyLength));
            populateRequestHeaders(request, requestParams);
            traceLogger.info(String.format("%s message=[Request Trace logging]", prepareForLogging(Utils.asSplunkLogString(requestParams))));
        }
    }

    Response logAndRebufferResponse(String configKey, Logger.Level logLevel, Response response, long elapsedTime) throws IOException {
        Request request = response.request();
        if (logOptions.isEnabled(getRequestHeaders(request))) {
            Map<String, String> responseParams = new LinkedHashMap<>();
            populateLoggerContextAttributes(responseParams);
            responseParams.put(LoggerConstants.LOG_LEVEL, logLevel.name());
            responseParams.put(LoggerConstants.EVENT_NAME, constructEventName(configKey));
            responseParams.put(LoggerConstants.HTTP_STATUS, Integer.toString(response.status()));
            responseParams.put(LoggerConstants.HTTP_REASON, '[' + response.reason() + ']');
            boolean success = response.status() >= 200 && response.status() < 300;
            if (!success) {
                responseParams.put(LoggerConstants.FAILURE_REASON, '[' + Integer.toString(response.status()) + " " + response.reason() + ']');
            }
            responseParams.put(LoggerConstants.SUCCESS, Boolean.toString(success));
            long start = System.currentTimeMillis();
            try {
                response = populateAndRebufferResponse(response, responseParams);
            } catch (IOException e) {
                if (logOptions.isRetryOnIOException()) {
                    throw Utils.convertToRetryableException(e, response);
                } else {
                    throw e;
                }
            } finally {
                long readDuration = System.currentTimeMillis() - start;
                responseParams.put(LoggerConstants.CONNECT_DURATION, Long.toString(elapsedTime));
                responseParams.put(LoggerConstants.READ_DURATION, Long.toString(readDuration));
                responseParams.put(LoggerConstants.DURATION, Long.toString(elapsedTime + readDuration));
            }
            populateResponseHeaders(response, responseParams);
            traceLogger.info(String.format("%s message=[Response Trace logging]", prepareForLogging(Utils.asSplunkLogString(responseParams))));
        }
        return response;
    }

    private Response populateAndRebufferResponse(Response response, Map<String, String> responseParams) throws IOException {
        responseParams.put(LoggerConstants.BODY_LENGTH, Long.toString(0));
        if (response.body() != null) {
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            int bodyLength = bodyData.length;
            responseParams.put(LoggerConstants.BODY_LENGTH, Long.toString(bodyLength));
            String body = Utils.decodeOrDefault(bodyData, UTF_8, BINARY_DATA);
            responseParams.put(LoggerConstants.RESPONSE, Utils.asSplunkMessage(body));
            response = response.toBuilder().body(bodyData).build();
        }
        return response;
    }

    private Map<String, Collection<String>> getRequestHeaders(Request request) {
        return request == null || request.headers() == null ? emptyMap() : request.headers();
    }

    private void populateLoggerContextAttributes(Map<String, String> requestParams) {
        requestParams.putAll(logOptions.getContextMap());
    }

    private void populateResponseHeaders(Response response, Map<String, String> params) {
        Map<String, Collection<String>> headers = Utils.defaultIfNull(response.headers(), emptyMap());
        populateHeaders(logOptions.getResponseHeadersToLog().stream()
                                  .collect(Collectors.toMap(h -> h, headers::get)), params);
    }

    private void populateHeaders(Map<String, Collection<String>> headers, Map<String, String> params) {
        if (headers != null) {
            for (String field : headers.keySet()) {
                for (String value : valuesOrEmpty(headers, field)) {
                    params.put(LoggerConstants.HEADER_PREFIX + field, Utils.asSplunkMessage(value));
                }
            }
        }
    }

    private String constructEventName(String configKey) {
        String[] args = configKey.split("[#]");
        String methodName = args.length == 2 ? args[1] : configKey;
        int index = methodName.indexOf('(');
        return index == -1 ? methodName : methodName.substring(0, index);
    }

    private void populateRequestHeaders(Request request, Map<String, String> params) {
        Map<String, Collection<String>> headers = Utils.defaultIfNull(request.headers(), emptyMap());
        populateHeaders(logOptions.getRequestHeadersToLog().stream()
                                  .collect(Collectors.toMap(h -> h, headers::get)), params);
    }

    private String prepareForLogging(String message) {
        AtomicReference<String> messageRef = new AtomicReference<>(message);
        try {
            if (logOptions.getSensitiveDataPattern() != null) {
                messageRef.set(logOptions.getSensitiveDataPattern().matcher(message).replaceAll(SENSITIVE_DATA_MASK));
            }
            if (logOptions.getSubstitutes() != null) {
                logOptions.getSubstitutes().forEach(subst -> {
                    if (subst.getPattern() != null) {
                        messageRef.set(subst.getPattern().matcher(messageRef.get()).replaceAll(subst.getValue()));
                    }
                });
            }
        } catch (Exception ignored) {
        }
        return messageRef.get();
    }

}
