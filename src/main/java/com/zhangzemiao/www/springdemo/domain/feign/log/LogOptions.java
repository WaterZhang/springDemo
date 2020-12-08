package com.zhangzemiao.www.springdemo.domain.feign.log;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class LogOptions {
    private Function<Map<String, Collection<String>>, Boolean> traceEnabled = (requestHeaders) -> false;
    private Supplier<Map<String, String>> contextMap = Collections::emptyMap;
    private Supplier<Set<String>> requestHeadersToLog = Collections::emptySet;
    private Supplier<Set<String>> responseHeadersToLog = Collections::emptySet;
    private Pattern sensitiveDataPattern;
    private List<Subst> substitutes = new ArrayList<>();
    private boolean retryOnIOException;

    public LogOptions traceEnabled(final Function<Map<String, Collection<String>>, Boolean> func) {
        this.traceEnabled = requireNonNull(func, "traceEnabled cannot be null");
        return this;
    }

    public LogOptions contextMap(final Supplier<Map<String, String>> contextMap) {
        this.contextMap = requireNonNull(contextMap, "contextMap cannot be null");
        return this;
    }

    public LogOptions requestHeadersToLog(final Supplier<Set<String>> requestHeadersToLog) {
        this.requestHeadersToLog = requireNonNull(requestHeadersToLog, "requestHeadersToLog cannot be null");
        return this;
    }

    public LogOptions responseHeadersToLog(final Supplier<Set<String>> responseHeadersToLog) {
        this.responseHeadersToLog = requireNonNull(responseHeadersToLog, "responseHeadersToLog cannot be null");
        return this;
    }

    public LogOptions hide(final Pattern sensitiveDataPattern) {
        // no null check for backward compatibility
        this.sensitiveDataPattern = sensitiveDataPattern;
        return this;
    }

    public LogOptions subst(final Pattern pattern, final String value) {
        this.substitutes.add(new Subst(
            requireNonNull(pattern, "pattern cannot be null"),
            requireNonNull(value, "value cannot be null")));
        return this;
    }

    public LogOptions retryOnIOException(final boolean retryOnIOException) {
        this.retryOnIOException = retryOnIOException;
        return this;
    }

    public boolean isEnabled(final Map<String, Collection<String>> requestHeaders) {
        return LogUtils.defaultIfNull(traceEnabled.apply(requestHeaders), false);
    }

    /**
     *
     * @return never null
     */
    public Map<String, String> getContextMap() {
        final Map<String, String> contextMap = this.contextMap.get();
        return LogUtils.defaultIfNull(contextMap, Collections.emptyMap());
    }

    /**
     *
     * @return never null
     */
    public Set<String> getRequestHeadersToLog() {
        return LogUtils.defaultIfNull(requestHeadersToLog.get(), Collections.emptySet());
    }

    /**
     *
     * @return never null
     */
    public Set<String> getResponseHeadersToLog() {
        return LogUtils.defaultIfNull(responseHeadersToLog.get(), Collections.emptySet());
    }

    public Pattern getSensitiveDataPattern() {
        return sensitiveDataPattern;
    }

    public List<Subst> getSubstitutes() {
        return substitutes;
    }

    public boolean isRetryOnIOException() {
        return retryOnIOException;
    }

    public static class Subst {
        private final Pattern pattern;
        private final String value;

        public Subst(final Pattern pattern, final String value) {
            this.pattern = pattern;
            this.value = value;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public String getValue() {
            return value;
        }
    }
}
