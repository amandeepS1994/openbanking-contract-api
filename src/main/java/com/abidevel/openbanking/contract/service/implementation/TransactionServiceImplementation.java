package com.abidevel.openbanking.contract.service.implementation;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;

import com.abidevel.openbanking.contract.integration.OpenBankingApi;
import com.abidevel.openbanking.contract.model.Transaction;
import com.abidevel.openbanking.contract.model.enumeration.TransactionType;
import com.abidevel.openbanking.contract.repository.TransactionRepository;
import com.abidevel.openbanking.contract.service.MerchantDetailService;
import com.abidevel.openbanking.contract.service.TransactionService;
import io.github.resilience4j.circuitbreaker.annotation.*;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class TransactionServiceImplementation implements TransactionService {

    private final OpenBankingApi openBankingApi;
    private final MerchantDetailService merchantDetailService;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImplementation (OpenBankingApi openBankingApi, MerchantDetailService merchantDetailService, TransactionRepository transactionRepository) {
        this.openBankingApi = openBankingApi;
        this.merchantDetailService = merchantDetailService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @CircuitBreaker(name = "open-banking-breaker", fallbackMethod = "openBankingFallback")
    @PreFilter("filterObject.accountNumber == accountNumber")
    public List<Transaction> findAllByAccountNumber(Long accountNumber) {
        return openBankingApi.findAllTransactionsByAccountNumber(accountNumber);
        // enrich the transaction information
    }

    private List<Transaction> openBankingFallback (Long accountNumber, final Throwable throwable) {
        log.info("Circuit Breaker.");
       // return transactionRepository.findByAccountNumber(accountNumber);
        return transactionRepository.findAll();
    }
    
}
