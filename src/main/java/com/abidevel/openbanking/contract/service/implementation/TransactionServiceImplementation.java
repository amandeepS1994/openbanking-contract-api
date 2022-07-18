package com.abidevel.openbanking.contract.service.implementation;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    // @PreFilter("filterObject.accountNumber == accountNumber")
    public List<Transaction> findAllByAccountNumber(Long accountNumber) {
        List<Transaction> transactions = openBankingApi.findAllTransactionsByAccountNumber(accountNumber);
            transactions.stream()
            .filter(Objects::nonNull)
            .forEach(tran -> tran.setMerchantLogo(merchantDetailService.retrieveMerchantLogo(tran.getMerchantName()).orElse(String.format("%s.png", tran.getMerchantName()))));
        return transactions;
    }

    private List<Transaction> openBankingFallback (Long accountNumber, final Throwable throwable) {
        log.info("Circuit Breaker.");
        log.info(throwable.getMessage());
        return transactionRepository.findAll();
    }
    
}
