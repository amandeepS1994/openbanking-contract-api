package com.abidevel.openbanking.contract.configuration.web;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import com.abidevel.openbanking.contract.configuration.WebConfiguration;
import com.abidevel.openbanking.contract.configuration.interceptor.OpenBankRequestInterceptor;

@Configuration
@PropertySource(value = "classpath:endpoint.properties")
public class RestTemplateConfiguration {

    private final WebConfiguration webConfig;
    private final OpenBankRequestInterceptor openBankRequestInterceptor;
    

    public RestTemplateConfiguration (WebConfiguration webConfiguration, OpenBankRequestInterceptor openBankRequestInterceptor) {
        this.webConfig = webConfiguration;
        this.openBankRequestInterceptor = openBankRequestInterceptor;
    }

    @Bean(name = "baseResttemplate")
    @Primary
    RestTemplate configureRestTemplate (RestTemplateBuilder builder) {
        return builder.rootUri(webConfig.getBaseUrl())
            .build();
    }


    @Bean(name = "obResttemplate")
    RestTemplate configureOpenBankingTestnetRestTemplate (RestTemplateBuilder builder) {
       return builder.rootUri(webConfig.getTestNet())
            .interceptors(openBankRequestInterceptor)
            .build();    
    }

}
