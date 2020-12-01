package com.zhangzemiao.www.springdemo.log;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;

public class MDCContextBuilder {
    static final private String DEFAULT_VALUE = "<not set>";
    static final private String HEADER_HOST = "Host";
    static final private String HEADER_USER_AGENT = "User-Agent";
    static final private String HEADER_REFERER = "Referer";
    static final private String HEADER_ORIGIN_REQUEST_ID = "ORIGIN-REQUEST-ID";
    public static final String PAGE_NAME_ATTR = "pageName";

    static final private List<String> HIDE_THESE_REQUEST_HEADERS =
        Arrays.asList("accept", "accept-encoding", "accept-language", "cookie", "connection", "host", "user-agent",
                      "referer", "cache-control", "authorization", "authentication");
    static final private List<String> HIDE_THESE_QUERY_PARAMS =
        Arrays.asList(new String[] { });

    public Map<String, String> buildMDCContext(HttpServletRequest request, Object handler) {
        final Map<String, String> contextMap = new HashMap<>();
        if(request != null) {
            populateServletContext(request, handler, contextMap);
        }
        return contextMap;
    }

    private void populateServletContext(HttpServletRequest request, Object handler, Map<String, String> contextMap) {
        {
            contextMap.put("requestUrl", request.getRequestURL().toString());
            contextMap.put("host", StringUtils.defaultString(request.getHeader(HEADER_HOST), DEFAULT_VALUE));
            contextMap.put("userAgent", StringUtils.defaultString(request.getHeader(HEADER_USER_AGENT), DEFAULT_VALUE));
            contextMap.put("clientIPAddress", StringUtils.defaultString(request.getRemoteAddr(), DEFAULT_VALUE));
            contextMap.put(PAGE_NAME_ATTR, getPageName(request, handler));
            contextMap.put("referer", StringUtils.defaultString(request.getHeader(HEADER_REFERER), DEFAULT_VALUE));
            contextMap.put("awsRegion", StringUtils.trimToNull(System.getenv().get("AWS_REGION")));

            // if there is no ORIGIN-REQUEST-ID header then set it to requestId
            String originRequestId = StringUtils.defaultString(request.getHeader(HEADER_ORIGIN_REQUEST_ID),
                                                               (String) contextMap.getOrDefault("requestId", DEFAULT_VALUE));
            contextMap.put("originRequestId", originRequestId);

            final Map<String, String> requestHeaders = getRequestHeaders(request);
            for (String header : requestHeaders.keySet()) {
                if (!HIDE_THESE_REQUEST_HEADERS.contains(header.toLowerCase())) {
                    contextMap.put("requestHeader_" + header, requestHeaders.get(header));
                }
            }

            final Map<String, List<String>> queryParams = getQueryParams(request);
            for (String param : queryParams.keySet()) {
                if (!HIDE_THESE_QUERY_PARAMS.contains(param.toLowerCase())) {
                    contextMap.put("queryParam_" + param, StringUtils.join(queryParams.get(param), ","));
                }
            }
        }
    }

    private String getPageName(HttpServletRequest request, Object handler){
        String pageId;
        if (handler instanceof HandlerMethod) {
            final HandlerMethod method = (HandlerMethod) handler;
            PageName annotation = method.getMethodAnnotation(PageName.class);
            if (annotation == null) {
                annotation = method.getBeanType().getAnnotation(PageName.class);
            }
            if (annotation != null) {
                pageId = annotation.value();
            } else {
                pageId = method.getBeanType().getSimpleName() + "." + method.getMethod().getName();
            }
        } else {
            pageId = "undefined";
        }
        return pageId;
    }

    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        final Map<String, String> requestHeaders = new HashMap<>();
        final Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            final String header = enumeration.nextElement();
            if (StringUtils.isNotBlank(header)) {
                requestHeaders.put(header, request.getHeader(header));
            }
        }
        return requestHeaders;
    }

    private Map<String, List<String>> getQueryParams(HttpServletRequest request) {
        if (StringUtils.isEmpty(request.getQueryString())) {
            return Collections.emptyMap();
        }
        return Arrays.stream(request.getQueryString().split("&"))
                     .map(this::splitQueryParameter)
                     .collect(Collectors.groupingBy(AbstractMap.SimpleImmutableEntry::getKey, LinkedHashMap::new, mapping(Map.Entry::getValue, toList())));
    }

    private AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
        final int idx = it.indexOf("=");
        final String key = idx > 0 ? it.substring(0, idx) : it;
        final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

}
