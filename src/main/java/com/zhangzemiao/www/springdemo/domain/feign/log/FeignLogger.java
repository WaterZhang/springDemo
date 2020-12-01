package com.zhangzemiao.www.springdemo.domain.feign.log;

import feign.Logger;
import feign.Request;
import feign.Response;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class FeignLogger extends Logger {
    private final FeignLoggerImpl feignLoggerImpl;

    /**
     * Create feign logger that prints request/response.
     * It does not take into account differences between {@link feign.Logger.Level} levels
     * and prints data if level is bigger than {@code Level.NONE}
     * @param contextMap
     */
    public FeignLogger(final Map<String, String> contextMap) {
        this(true, contextMap);
    }

    public FeignLogger(LogOptions logOptions) {
        this(new FeignLoggerImpl(new LoggerFactoryProvider(), logOptions));
    }

    /**
     * use {@link #FeignLogger(LogOptions)}
     * @param loggerContext
     */
    public FeignLogger(ILoggingAwareContext loggerContext) {
        this(new FeignLoggerImpl(new LoggerFactoryProvider(),
                                 new LogOptions()
                                     .traceEnabled(loggerContext::isTraceEnabled)
                                     .contextMap(loggerContext::getContextMap)));
    }

    /**
     * use {@link #FeignLogger(LogOptions)}
     * @param isTraceEnabled
     * @param contextMap
     */
    public FeignLogger(final boolean isTraceEnabled, final Map<String, String> contextMap) {
        this(new LogOptions()
                 .traceEnabled((requestHeadres) -> isTraceEnabled)
                 .contextMap(() -> contextMap));
    }


    /**
     * use {@link #FeignLogger(LogOptions)}
     * @param isTraceEnabled
     * @param contextMap
     * @param sensitiveDataPattern
     */
    public FeignLogger(final boolean isTraceEnabled,
                       final Map<String, String> contextMap,
                       final Pattern sensitiveDataPattern) {
        this(new LogOptions()
                 .traceEnabled((requestHeadres) -> isTraceEnabled)
                 .contextMap(() -> contextMap)
                 .hide(sensitiveDataPattern));
    }

    /**
     * use {@link #FeignLogger(LogOptions)}
     * @param isTraceEnabled
     * @param contextMap
     * @param sensitiveDataPattern
     */
    public FeignLogger(final Function<Map<String, Collection<String>>, Boolean> isTraceEnabled,
                       final Supplier<Map<String, String>> contextMap,
                       final Pattern sensitiveDataPattern) {


        this(new LogOptions()
                 .traceEnabled(isTraceEnabled)
                 .contextMap(contextMap)
                 .hide(sensitiveDataPattern));
    }

    /**
     * Package visible constructor for tests
     */
    FeignLogger(FeignLoggerImpl feignLoggerImpl) {
        this.feignLoggerImpl = feignLoggerImpl;
    }


    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        feignLoggerImpl.logRequest(configKey, logLevel, request);
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        return feignLoggerImpl.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        feignLoggerImpl.log(configKey, format, args);
    }
}
