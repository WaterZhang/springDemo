package com.zhangzemiao.www.springdemo.domain.feign.interceptor;

import com.zhangzemiao.www.springdemo.domain.feign.plugin.SystemPropertyProvider;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;

public class RequestClientIdInterceptor implements RequestInterceptor {
    static final private  String CLIENT_ID = "clientId";
    static final private  String APPLICATION_NAME_PROPERTY = "application.name";

    private SystemPropertyProvider systemPropertyProvider = new SystemPropertyProvider();

    @Override
    public void apply(RequestTemplate template) {
        String clientIdValue = systemPropertyProvider.getProperty(APPLICATION_NAME_PROPERTY);

        if(StringUtils.isNotEmpty(clientIdValue))
            template.header(CLIENT_ID, clientIdValue);
    }
}
