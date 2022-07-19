package com.abidevel.openbanking.contract.service.implementation;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
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
        return insertMarchantLogos(openBankingApi.findAllTransactionsByAccountNumber(accountNumber));
    }

    private List<Transaction> insertMarchantLogos (List<Transaction> inputTransactions) {
        inputTransactions.stream()
            .filter(Objects::nonNull)
            .forEach(tran -> tran.setMerchantLogo(merchantDetailService.retrieveMerchantLogo(tran.getMerchantName()).orElse(String.format("%s.png", tran.getMerchantName()))));
        return inputTransactions;
    }

    private List<Transaction> openBankingFallback (Long accountNumber, final Throwable throwable) {
        log.info("Circuit Breaker.");
        log.info(throwable.getMessage());
        return transactionRepository.findAll();
    }

    @Scheduled(fixedRate = 300000l)
    public void updateTransactionList() {
         // Gather all unique Ids
        log.info("Executing Scheduling");
        List<Long> accountIds = retrieveAllAccountIds();
        if (!accountIds.isEmpty()) {
            accountIds.forEach(id -> pollByAccountNumber(id));
        }
    }

    private void pollByAccountNumber (Long accountNumber) {
        // retrieve all transactions from API
        log.info("Retrieving transactions.");
        List<Transaction> obTransactions = this.openBankingApi.findAllTransactionsByAccountNumber(accountNumber);
        List<Transaction> dbTransactions = transactionRepository.findByAccountNumber(accountNumber);
        // remove transactions
        obTransactions.removeAll(dbTransactions);
        log.info("Persisting Transactions " + Integer.toString(obTransactions.size()));
        // if theres a difference then persist on the database
        transactionRepository.saveAll(obTransactions);
        
    }

    private List<Long> retrieveAllAccountIds () {
        return this.transactionRepository.findAllDistinct();
    }



    
}
