package com.cards.cards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket swaggerConfiguration()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/cards/**"))
                .apis(RequestHandlerSelectors.basePackage("com.cards.cards"))
                .build()
                .apiInfo(apiDetails());
    }
    private ApiInfo apiDetails()
    { return new ApiInfo("Online Bank Application",
            "Its a online bank application","1.0","Free to use",
            new Contact("Tayyaba","Http://localhost:8080","tayyaba.sani@yahoo.com"),
            "API","Http://localhost:8080", Collections.emptyList()
    );

    }
}
