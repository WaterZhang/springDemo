package com.zhangzemiao.www.springdemo.web;

import com.zhangzemiao.www.springdemo.servlet.BuildInfoServlet;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class InitSupport implements TomcatConnectorCustomizer, TomcatContextCustomizer {

    private final int httpPort;
    private final int httpsPort;

    @Autowired
    public InitSupport() {
        this.httpPort = 8080;
        this.httpsPort = 8443;
    }

    @Bean
    public ServletRegistrationBean<BuildInfoServlet> registerBuildInfo() {
        final ServletRegistrationBean<BuildInfoServlet> buildInfoBean =
            new ServletRegistrationBean<>(new BuildInfoServlet(), "/buildInfo");
        buildInfoBean.setName("buildInfo");
        return buildInfoBean;
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherServletRegistration() {
        final ServletRegistrationBean<DispatcherServlet> registration = new ServletRegistrationBean<>(
            dispatcherServlet(), "/zzm/*", "/");
        registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
        return registration;
    }

    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean() {
        return new DispatcherServletRegistrationBean(dispatcherServlet(), "/");
    }

    @Override
    public void customize(final Connector connector) {
        connector.setPort(httpPort);
        connector.setScheme("http");
        connector.setRedirectPort(httpsPort);
    }

    @Override
    public void customize(final Context context) {
        final SecurityConstraint securityConstraint = new SecurityConstraint();
        securityConstraint.setUserConstraint("CONFIDENTIAL");
        final SecurityCollection collection = new SecurityCollection();
        collection.addPattern("/*");
        securityConstraint.addCollection(collection);
        context.addConstraint(securityConstraint);
    }
}
