package com.zhangzemiao.www.springdemo.web;

import javax.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ConfigurationProperties
public class ServletContextConfig implements WebMvcConfigurer, ServletContextAware {

    private final Environment environment;
    private ServletContext servletContext;


    public ServletContextConfig(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/").addResourceLocations("/**");
    }

    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        return new CustomTomcatEmbeddedServletContainerFactory();
    }

    private static class CustomTomcatEmbeddedServletContainerFactory extends TomcatServletWebServerFactory {
        @Override
        protected void postProcessContext(final Context context) {
            ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
        }
    }

}
