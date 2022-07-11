package com.abidevel.openbanking.contract.integration.implementation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.ResolvableType;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.abidevel.openbanking.banking.model.OBTransaction6;
import com.abidevel.openbanking.contract.integration.OpenBankingApi;
import com.abidevel.openbanking.contract.model.Transaction;
import com.abidevel.openbanking.contract.model.Response.ApiResponse;
import com.abidevel.openbanking.contract.model.adapter.ObTransactionAdapter;

@Service
public class OpenBankingApiImplementation implements OpenBankingApi {

    private final RestTemplate restTemplate;

    public OpenBankingApiImplementation (RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Transaction> findAllTransactionsByAccountNumber(Long accountNumber) throws NullPointerException {
       ResponseEntity<ApiResponse> response = this.restTemplate.exchange(String.format("transactions/%x/", accountNumber), HttpMethod.GET, null, ApiResponse.class);
       return isValid(response) ?  toTransactionsEntity(response.getBody()) : Collections.emptyList();
    
    }
    

    private boolean isValid (ResponseEntity<ApiResponse> openBankingResponse) throws NullPointerException {
        return openBankingResponse.getStatusCode().is2xxSuccessful() && Objects.nonNull(openBankingResponse.getBody()) && openBankingResponse.getBody().isSuccessful();
    }

    private List<Transaction> toTransactionsEntity (ApiResponse<Object> dApiResponse) {
        if (dApiResponse.getData() instanceof Collections) {
             return  Stream.of(dApiResponse.getData())
                    .filter(OBTransaction6.class :: isInstance)
                    .map(OBTransaction6.class:: cast)
                    .map(ObTransactionAdapter::new)
                    .map(ObTransactionAdapter::getTransaction)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
