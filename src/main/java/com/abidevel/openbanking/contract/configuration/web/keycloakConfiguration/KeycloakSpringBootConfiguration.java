package com.abidevel.openbanking.contract.configuration.web.keycloakConfiguration;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakSpringBootConfiguration {

    // This will resolve Keycloak configuration in the application.properties file.
    // As described in the javadocs, the KeycloakSpringBootConfigResolver is defined in a seperate file to avoid
    // Circular Dependency.
    @Bean
    KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }


}