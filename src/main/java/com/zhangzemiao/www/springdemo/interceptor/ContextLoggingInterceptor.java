package com.zhangzemiao.www.springdemo.interceptor;

import com.zhangzemiao.www.springdemo.log.MDCContextBuilder;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ContextLoggingInterceptor extends HandlerInterceptorAdapter {

    private final MDCContextBuilder mdcContextBuilder = new MDCContextBuilder();
    private MDCWrapper mdcWrapper = new MDCWrapper();

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws IOException {

        final Map<String, String> map = mdcContextBuilder.buildMDCContext(request, handler);

        // Required for access log
        setRequestAttributes(request, map);

        map.forEach((key, value) -> mdcWrapper.put(key, value));
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Object handler,
                                final Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        mdcWrapper.clear();
    }

    private void setRequestAttributes(final HttpServletRequest request,
                                      final Map<String, String> contextMap) {
        if (contextMap != null) {
            request.setAttribute("requestId", StringUtils.defaultString(contextMap.get("requestId")));
            request.setAttribute("originRequestId",
                                 StringUtils.defaultString(contextMap.get("originRequestId")));
            request.setAttribute("pageName", StringUtils.defaultString(contextMap.get("pageName")));
        }
    }

    private static class MDCWrapper {

        public void put(final String key, final String val) throws IllegalArgumentException {
            MDC.put(key, Objects.toString(val));
        }

        public void clear() {
            MDC.clear();
        }
    }
}
