package com.abidevel.openbanking.contract.configuration.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource(value = "classpath:endpoint.properties")
public class RestTemplateConfiguration {
    
    private final String baseUrl;

    public RestTemplateConfiguration (@Value("S{spring.banking.baseUrl}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Bean
    public RestTemplate configureRestTemplate (RestTemplateBuilder builder) {
        return builder.rootUri(baseUrl)
            .build();
    }
}
