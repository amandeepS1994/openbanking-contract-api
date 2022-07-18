package com.abidevel.openbanking.contract.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@PropertySource("classpath:openBankingCredentials.properties")
@ConfigurationProperties(prefix = "ob")
@Data
public class OpenBankingCredentials {
    private String username;
    private String password;
}
