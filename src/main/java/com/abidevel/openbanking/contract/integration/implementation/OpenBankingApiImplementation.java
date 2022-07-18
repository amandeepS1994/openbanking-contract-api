package com.abidevel.openbanking.contract.integration.implementation;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.abidevel.openbanking.banking.model.OBReadTransaction6;
import com.abidevel.openbanking.banking.model.OBTransaction6;
import com.abidevel.openbanking.contract.configuration.OpenBankingCredentials;
import com.abidevel.openbanking.contract.configuration.WebConfiguration;
import com.abidevel.openbanking.contract.integration.OpenBankingApi;
import com.abidevel.openbanking.contract.model.Transaction;
import com.abidevel.openbanking.contract.model.Response.ApiResponse;
import com.abidevel.openbanking.contract.model.Response.OpenBankingAccessTokenResponse;
import com.abidevel.openbanking.contract.model.Response.OpenBankingAccessTransactionWrapperResponse;
import com.abidevel.openbanking.contract.model.adapter.ObTransactionAdapter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OpenBankingApiImplementation implements OpenBankingApi {

    private final RestTemplate openBankingRestTemplate;

    private final OpenBankingCredentials openBankingCredentials;

    private final WebConfiguration webConfiguration;

    public OpenBankingApiImplementation (@Qualifier("obResttemplate") RestTemplate openBankingRestTemplate, OpenBankingCredentials openBankingCredentials, WebConfiguration webConfiguration) {
        this.openBankingRestTemplate = openBankingRestTemplate;
        this.openBankingCredentials = openBankingCredentials;
        this.webConfiguration = webConfiguration;
    }

    @Override
    public List<Transaction> findAllTransactionsByAccountNumber(Long accountNumber) throws NullPointerException {
        List<OBTransaction6> transactions = makeRequest(accountNumber);
        return !transactions.isEmpty() ? toTransactionsEntity(transactions) :Collections.emptyList();    
    }

    private  List<OBTransaction6> makeRequest(Long accountNumber) {
        // Obtain the access token.
        Optional<String> accessToken = obtainAccessToken();
        if (accessToken.isPresent()) {
          try {
                // Perform Request.
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", String.format("Bearer %s", accessToken.get()));
                ResponseEntity<OBReadTransaction6> responseEntity = new RestTemplate().exchange(String.format("%saccounts/%d/transactions", this.webConfiguration.getTestNet(), accountNumber), HttpMethod.GET, new HttpEntity<String>(headers), OBReadTransaction6.class);
                if (Objects.nonNull(responseEntity) && responseEntity.getStatusCode().is2xxSuccessful() && !responseEntity.getBody().getData().getTransaction().isEmpty()) {
                    return responseEntity.getBody().getData().getTransaction();
                }
          } catch (HttpClientErrorException hse) {                   
            log.error("Exception response is {}", hse.getResponseBodyAsString()); 
          }
    
        }
        return Collections.emptyList();
    }
    

    private boolean isValid (ResponseEntity<ApiResponse> openBankingResponse) throws NullPointerException {
        return openBankingResponse.getStatusCode().is2xxSuccessful() && Objects.nonNull(openBankingResponse.getBody()) && openBankingResponse.getBody().isSuccessful();
    }

    private List<Transaction> toTransactionsEntity (List<OBTransaction6> transactions) {
        return transactions.stream()
                    .map(ObTransactionAdapter::new)
                    .map(ObTransactionAdapter::getTransaction)
                    .collect(Collectors.toList());
    }



    private Optional<String> obtainAccessToken () {
    try {
        ResponseEntity<OpenBankingAccessTokenResponse> responseEntity = new RestTemplate().exchange(String.format("%soauth/token", this.webConfiguration.getTestNet()), HttpMethod.POST, generateHttpEntity(), OpenBankingAccessTokenResponse.class);
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
