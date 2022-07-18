package com.abidevel.openbanking.contract.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@PropertySource("classpath:endpoint.properties")
@ConfigurationProperties(prefix = "spring.banking")
@Data
public class WebConfiguration {

    private String baseUrl;
    private String testNet;

    
    
}
