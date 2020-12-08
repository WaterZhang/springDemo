package com.zhangzemiao.www.springdemo.domain.feign;

import static feign.ExceptionPropagationPolicy.NONE;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.zhangzemiao.www.springdemo.domain.feign.interceptor.RequestClientIdInterceptor;
import com.zhangzemiao.www.springdemo.domain.feign.log.FeignLogger;
import com.zhangzemiao.www.springdemo.domain.feign.log.LogOptions;
import com.zhangzemiao.www.springdemo.domain.feign.log.LogUtils;
import com.zhangzemiao.www.springdemo.domain.feign.settingfactory.SetterFactoryByMethodName;
import feign.Client;
import feign.ExceptionPropagationPolicy;
import feign.FeignException;
import feign.Logger;
import feign.QueryMapEncoder;
import feign.Request;
import feign.RequestInterceptor;
import feign.Response;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import feign.querymap.BeanQueryMapEncoder;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.slf4j.MDC;

public class HystrixFeignBuilder {

    private final List<RequestInterceptor> requestInterceptors = new ArrayList<>();
    private Client client = new Client.Default(null, null);
    private Retryer retryer = new Retryer.Default(100, SECONDS.toMillis(1), 0);
    private Encoder encoder = new Encoder.Default();
    private Decoder decoder = new Decoder.Default();
    private ErrorDecoder errorDecoder = new ErrorDecoder.Default();
    private Request.Options options = new Request.Options();
    private boolean decode404;
    private LogOptions logOptions = new LogOptions();
    private HystrixFeignBuilderFactory builderFactory = new HystrixFeignBuilderFactory();
    private InterceptorsFactory interceptorsFactory = new InterceptorsFactory();
    private SetterFactory setterFactory = new SetterFactoryByMethodName();
    private boolean retryOnIOException;
    private QueryMapEncoder queryMapEncoder = new BeanQueryMapEncoder();
    private ExceptionPropagationPolicy propagationPolicy = NONE;

    public <T> T build(final Class<T> apiType, final String url) {
        return build(apiType, url, null);
    }

    public <T> T build(final Class<T> apiType, final String url, final T fallback) {
        requestInterceptor(interceptorsFactory.getClientIdInterceptor());

        HystrixFeign.Builder builder =
            ((HystrixFeign.Builder) builderFactory.builder()
                                                  .client(client)
                                                  .logLevel(Logger.Level.FULL)
                                                  .logger(new FeignLogger(logOptions.retryOnIOException(retryOnIOException)))
                                                  .encoder(encoder)
                                                  .decoder(new LoggingDecoderWrapper(decoder, retryOnIOException))
                                                  .errorDecoder(errorDecoder)
                                                  .options(options)
                                                  .retryer(retryer)
                                                  .requestInterceptors(requestInterceptors)
                                                  .queryMapEncoder(queryMapEncoder)
                                                  .exceptionPropagationPolicy(propagationPolicy))
                .setterFactory(setterFactory);
        if (decode404) {
            builder = builder.decode404();
        }

        return builder.target(apiType, url, fallback);
    }

    public HystrixFeignBuilder client(final Client client) {
        this.client = client;
        return this;
    }

    public HystrixFeignBuilder retryer(final Retryer retryer) {
        this.retryer = retryer;
        return this;
    }

    public HystrixFeignBuilder encoder(final Encoder encoder) {
        this.encoder = encoder;
        return this;
    }

    public HystrixFeignBuilder decoder(final Decoder decoder) {
        this.decoder = decoder;
        return this;
    }

    public HystrixFeignBuilder decode404() {
        this.decode404 = true;
        return this;
    }

    public HystrixFeignBuilder errorDecoder(final ErrorDecoder errorDecoder) {
        this.errorDecoder = errorDecoder;
        return this;
    }

    public HystrixFeignBuilder options(final Request.Options options) {
        this.options = options;
        return this;
    }

    public HystrixFeignBuilder setterFactory(final SetterFactory setterFactory) {
        this.setterFactory = setterFactory;
        return this;
    }

    public HystrixFeignBuilder queryMapEncoder(final QueryMapEncoder queryMapEncoder) {
        this.queryMapEncoder = queryMapEncoder;
        return this;
    }

    public HystrixFeignBuilder exceptionPropagationPolicy(final ExceptionPropagationPolicy propagationPolicy) {
        this.propagationPolicy = propagationPolicy;
        return this;
    }

    public HystrixFeignBuilder requestInterceptor(final RequestInterceptor requestInterceptor) {
        this.requestInterceptors.add(requestInterceptor);
        return this;
    }

    public HystrixFeignBuilder requestInterceptors(final Iterable<RequestInterceptor> requestInterceptors) {
        this.requestInterceptors.clear();
        for (final RequestInterceptor requestInterceptor : requestInterceptors) {
            this.requestInterceptors.add(requestInterceptor);
        }
        return this;
    }

    public HystrixFeignBuilder traceEnabled(final LogOptions logOptions) {
        this.logOptions = Objects.requireNonNull(logOptions, "logOptions cannot be null");
        return this;
    }

    public HystrixFeignBuilder traceEnabled(final boolean traceEnabled, final Map<String, String> contextMap) {
        return traceEnabled((requestHeaders) -> traceEnabled,
                            () -> contextMap == null ? Collections.emptyMap() : contextMap, (Pattern) null);
    }

    public HystrixFeignBuilder traceEnabled(final boolean traceEnabled,
                                            final Map<String, String> contextMap,
                                            final String sensitiveDataPattern) {
        return traceEnabled(traceEnabled, contextMap,
                            sensitiveDataPattern == null ? null : Pattern.compile(sensitiveDataPattern));
    }

    public HystrixFeignBuilder traceEnabled(final boolean traceEnabled,
                                            final Map<String, String> contextMap,
                                            final Pattern sensitiveDataPattern) {
        return traceEnabled((requestHeaders) -> traceEnabled,
                            () -> contextMap == null ? Collections.emptyMap() : contextMap,
                            sensitiveDataPattern);
    }

    public HystrixFeignBuilder traceEnabled(final Function<Map<String, Collection<String>>, Boolean> traceEnabled,
                                            final Supplier<Map<String, String>> contextMap,
                                            final String sensitiveDataPattern) {
        return traceEnabled(traceEnabled, contextMap,
                            sensitiveDataPattern == null ? null : Pattern.compile(sensitiveDataPattern));
    }

    public HystrixFeignBuilder traceEnabled(final Function<Map<String, Collection<String>>, Boolean> traceEnabled,
                                            final Supplier<Map<String, String>> contextMap,
                                            final Pattern sensitiveDataPattern) {
        this.logOptions = new LogOptions()
                              .traceEnabled(traceEnabled)
                              .contextMap(contextMap)
                              .hide(sensitiveDataPattern);
        return this;
    }

    public HystrixFeignBuilder retryOnIOException(final boolean retryOnIOException) {
        this.retryOnIOException = retryOnIOException;
        return this;
    }

    /* default */ class InterceptorsFactory {
        private RequestClientIdInterceptor getClientIdInterceptor() {
            return new RequestClientIdInterceptor();
        }
    }

    /* default */ class HystrixFeignBuilderFactory {
        private HystrixFeign.Builder builder() {
            return HystrixFeign.builder();
        }
    }

    private static class LoggingDecoderWrapper implements Decoder {
        private final Decoder decoder;
        private final boolean retryOnIOException;

        public LoggingDecoderWrapper(final Decoder decoder, final boolean retryOnIOException) {
            this.decoder = Objects.requireNonNull(decoder, "Decoder cannot be null");
            this.retryOnIOException = retryOnIOException;
        }

        @Override
        public Object decode(final Response response, final Type type) throws IOException, FeignException {
            final long start = System.currentTimeMillis();
            try {
                return decoder.decode(response, type);
            } catch (IOException e) {
                if (retryOnIOException) {
                    throw LogUtils.convertToRetryableException(e, response);
                } else {
                    throw e;
                }
            } finally {
                MDC.put("decoderDuration", Long.toString(System.currentTimeMillis() - start));
            }
        }
    }
}
