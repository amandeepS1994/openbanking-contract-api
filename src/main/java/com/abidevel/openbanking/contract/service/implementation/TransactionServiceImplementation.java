package com.abidevel.openbanking.contract.service.implementation;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.abidevel.openbanking.contract.model.Transaction;
import com.abidevel.openbanking.contract.model.enumeration.TransactionType;
import com.abidevel.openbanking.contract.service.TransactionService;

@Service
public class TransactionServiceImplementation implements TransactionService{

    @Override
    public List<Transaction> findAllByAccountNumber(Long accountNumber) {
        return List.of(
            new Transaction(1l, TransactionType.CASH, new Date(), accountNumber, "£", 12345678l, "Bookers", "merchantLogo.svg"),
            new Transaction(2l, TransactionType.CRYPTO, new Date(), accountNumber, "£", 12345678l, "Crypto", "crypto.com.svg"),
            new Transaction(3l, TransactionType.CHEQUE, new Date(), accountNumber, "£", 12345678l, "Taj", "Taj.svg"));
    }
    
}
