package com.abidevel.openbanking.contract.service.implementation;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.abidevel.openbanking.contract.integration.OpenBankingApi;
import com.abidevel.openbanking.contract.model.Transaction;
import com.abidevel.openbanking.contract.model.enumeration.TransactionType;
import com.abidevel.openbanking.contract.service.MerchantDetailService;
import com.abidevel.openbanking.contract.service.TransactionService;
import io.github.resilience4j.circuitbreaker.annotation.*;


@Service
public class TransactionServiceImplementation implements TransactionService {

    private final OpenBankingApi openBankingApi;
    private final MerchantDetailService merchantDetailService;

    public TransactionServiceImplementation (OpenBankingApi openBankingApi, MerchantDetailService merchantDetailService) {
        this.openBankingApi = openBankingApi;
        this.merchantDetailService = merchantDetailService;
    }

    @Override
    @CircuitBreaker(name = "open-banking-breaker", fallbackMethod = "openBankingFallback")
    public List<Transaction> findAllByAccountNumber(Long accountNumber) {
        //openBankingApi.findAllTransactionsByAccountNumber(accountNumber);
        // enrich the transaction information
        return List.of(
            new Transaction(1l, TransactionType.CASH.name(), OffsetDateTime.now(), accountNumber, "£", 12345678l, "Bookers", "merchantLogo.svg"),
            new Transaction(2l, TransactionType.CRYPTO.name(), OffsetDateTime.now(), accountNumber, "£", 12345678l, "Crypto", "crypto.com.svg"),
            new Transaction(3l, TransactionType.CHEQUE.name(), OffsetDateTime.now(), accountNumber, "£", 12345678l, "Taj", "Taj.svg"));
    }

    private List<Transaction> openBankingFallback (Long accountNumber, final Throwable throwable) {
        return Collections.emptyList();
    }
    
}
