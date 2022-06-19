package com.abidevel.openbanking.contract.service;

import java.util.List;

import com.abidevel.openbanking.contract.model.Transaction;

public interface TransactionService {
    List<Transaction> findAllByAccountNumber(Long accountNumber);
}
