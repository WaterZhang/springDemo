package com.zhangzemiao.www.springdemo.web;

import com.zhangzemiao.www.springdemo.interceptor.ContextLoggingInterceptor;
import com.zhangzemiao.www.springdemo.interceptor.HystrixInitializingInterceptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ConfigurationProperties
public class ServletContextConfig implements WebMvcConfigurer, ServletContextAware {

    private final Environment environment;
    private ServletContext servletContext;
    private final HystrixInitializingInterceptor hystrixInitializingInterceptor;
    private static final String[] REST_APIS = {
        "/api/*",
        "/api/**",
    };


    public ServletContextConfig(final Environment environment) {
        this.environment = environment;
        final Map<String, String> configuration = loadConfigProperties();
        this.hystrixInitializingInterceptor = new HystrixInitializingInterceptor(true, true, configuration);
    }

    private Map<String, String> loadConfigProperties() {
        final Map<String, String> config = new HashMap<>();
        for(final PropertySource<?> propertySource : ((ConfigurableEnvironment) environment).getPropertySources()){
            if(propertySource instanceof EnumerablePropertySource){
                for( final String key : ((EnumerablePropertySource) propertySource).getPropertyNames() ) {
                    config.put(key, Objects.toString(propertySource.getProperty(key)));
                }
            }
        }
        return config;
    }


    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(hystrixInitializingInterceptor)
                .addPathPatterns(REST_APIS)
                .order(1);

        registry.addInterceptor(new ContextLoggingInterceptor())
                .addPathPatterns(REST_APIS);
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

    public ServletContext getServletContext() {
        return servletContext;
    }
}
