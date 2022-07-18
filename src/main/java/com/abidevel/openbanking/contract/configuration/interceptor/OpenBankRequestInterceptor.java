package com.abidevel.openbanking.contract.configuration.interceptor;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.abidevel.openbanking.contract.configuration.OpenBankingCredentials;
import com.abidevel.openbanking.contract.configuration.WebConfiguration;
import com.abidevel.openbanking.contract.model.Response.OpenBankingAccessTokenResponse;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class OpenBankRequestInterceptor implements ClientHttpRequestInterceptor {

    private final OpenBankingCredentials openBankingCredentials;
    private final WebConfiguration webConfiguration;


    public OpenBankRequestInterceptor(OpenBankingCredentials openBankingCredentials, WebConfiguration webConfiguration) {
        this.openBankingCredentials = openBankingCredentials;
        this.webConfiguration = webConfiguration;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
                log.info("Intercepting Request.");
                Optional<String> accessToken = obtainAccessToken(new RestTemplate());
                if (accessToken.isPresent()) {
                    prependRequestWithAccessToken(request, accessToken.get());
                    execution.execute(request, body);
                }
             
        return null;
    }



    private void prependRequestWithAccessToken(HttpRequest request, String accessToken) {
        request.getHeaders().add("Authorization", String.format("Bearer %s", accessToken));
        log.info("Instrumentation Complete.");
    }

    private Optional<String> obtainAccessToken (RestTemplate restTemplate) {
    try {
        ResponseEntity<OpenBankingAccessTokenResponse> responseEntity = restTemplate.exchange(String.format("%s/oauth/token", webConfiguration.getTestNet()), HttpMethod.POST, generateHttpEntity(), OpenBankingAccessTokenResponse.class);
        return responseEntity.getStatusCode().is2xxSuccessful() && Objects.nonNull(responseEntity.getBody()) ?
            Optional.ofNullable(responseEntity.getBody().getAccess_token()) : Optional.empty(); 
    } catch (RestClientException ex) {
        log.error("Failed to Obtain Access Token", ex);
        return Optional.empty();
    } catch (NullPointerException nullPointerException) {
        log.error("Access Token Unavailable.", nullPointerException);
        return Optional.empty();
    }
    }

    private HttpEntity<MultiValueMap<String, String>> generateHttpEntity () {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", String.format("Basic %s", retrieveEncodedCred()));
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formBody = new LinkedMultiValueMap<>();
        formBody.add("grant_type", "client_credentials");
        return new HttpEntity<MultiValueMap<String,String>>(formBody, httpHeaders);
    }

    private String retrieveEncodedCred() {
        return new String(
            Base64.getEncoder().encode(
            String.format("%s:%s", openBankingCredentials.getUsername(), openBankingCredentials.getPassword())
        .getBytes())); 
    }

}
