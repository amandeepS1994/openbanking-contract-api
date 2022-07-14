package com.abidevel.openbanking.contract.configuration.web.keycloakConfiguration;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;

import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

// Annotation provides all annotations that are needed to integrate Keycloak in Spring Security (e.g. for enabling Spring Security or configuring component scanning)
// By extending KeycloakWebSecurityConfigurerAdapter, we gain a useful base class,
// for creating a WebSecurityConfigurer instance secured by Keycloak
@EnableGlobalMethodSecurity(prePostEnabled = true)
@KeycloakConfiguration
public class KeycloakConfig extends KeycloakWebSecurityConfigurerAdapter{ 

// We have to register the KeycloakAuthenticationProvider with the authentication manager.
 @Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) { 
    auth.authenticationProvider(getKeycloakAuthenticationProvider());
}

// Auxiliary method for customising the authentication provider. 
// Providing a simple one-to-one GrantedAuthoritiesMapper, which adds the ROLE_ prefix and converts the authority value to upper case 
// (e.g. a chief-operating-officer role from Keycloak realm becomes ROLE_CHIEF-OPERATING-OFFICER)

private KeycloakAuthenticationProvider getKeycloakAuthenticationProvider() { 
    KeycloakAuthenticationProvider authenticationProvider = keycloakAuthenticationProvider();
    var mapper = new SimpleAuthorityMapper();
    mapper.setConvertToUpperCase(true);
    authenticationProvider.setGrantedAuthoritiesMapper(mapper);
    return authenticationProvider;
}


// Overwrite Spring Security behaviour, first by calling the parent implementation and then by adding our own csrf and endpoint protection config
@Override
protected void configure(HttpSecurity http) throws Exception { //3
    super.configure(http);
        http
            .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            .authorizeRequests()
            .anyRequest().authenticated();
}

// The session authentication strategy bean has to be of type RegisterSessionAuthenticationStrategy for public or confidential applications (NullAuthenticatedSessionStrategy for bearer-only applications).
@Bean
@Override
protected SessionAuthenticationStrategy sessionAuthenticationStrategy() { 
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
}



}
