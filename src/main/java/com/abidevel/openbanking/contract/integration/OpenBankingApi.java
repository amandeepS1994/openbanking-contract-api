package com.abidevel.openbanking.contract.integration;

import com.abidevel.openbanking.contract.model.Transaction;

import java.util.List;

public interface OpenBankingApi {
    List<Transaction> findAllTransactionsByAccountNumber (Long accountNumber);
}
