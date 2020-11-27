package com.zhangzemiao.www.springdemo.web;

import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ModelSpecification;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

    @Value("${swagger.email}")
    private String email;
    @Value("${swagger.description}")
    private String description;
    @Value("${swagger.version}")
    private String version;
    @Value("${swagger.title}")
    private String title;

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                   .apiInfo(apiInfo())
                   .select()
                   .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                   .build().globalRequestParameters(globalOperationParameters());
    }

    private ApiInfo apiInfo() {
        final Contact contact = new Contact("Water Zemiao Zhang", null, email);
        return new ApiInfoBuilder()
                   .title(title)
                   .description(description)
                   .version(version)
                   .contact(contact)
                   .build();
    }

    List<RequestParameter> globalOperationParameters(){
        final List<RequestParameter> parameters = new ArrayList<>();
        final RequestParameter clientId =
            new RequestParameterBuilder()
                .name("clientId")
                .description("clientId")
                .in(ParameterType.HEADER)
                .required(false).build();

        parameters.add(clientId);
        return parameters;
    }

}
