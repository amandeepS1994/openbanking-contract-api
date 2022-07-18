package com.abidevel.openbanking.contract.configuration.filter;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import com.abidevel.openbanking.contract.configuration.OpenBankingCredentials;
import com.abidevel.openbanking.contract.configuration.WebConfiguration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ObtainOpenAccessTokenFilter extends OncePerRequestFilter {

    private final WebConfiguration webConfiguration;
    private final OpenBankingCredentials openBankingCredentials;

    public ObtainOpenAccessTokenFilter(WebConfiguration webConfiguration, OpenBankingCredentials openBankingCredentials) {
        this.webConfiguration = webConfiguration;
        this.openBankingCredentials = openBankingCredentials;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                super.doFilter(request, response, filterChain);
        
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().equalsIgnoreCase("/accounts/1234567/transactions");
    }

    
    
}
