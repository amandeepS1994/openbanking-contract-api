package com.abidevel.openbanking.contract.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.abidevel.openbanking.contract.ContractApplicationTests;
import com.abidevel.openbanking.contract.model.Transaction;

class TransactionServiceTest extends ContractApplicationTests {

    @Autowired
    private TransactionService transactionService;

    @BeforeAll()
    static void setUp () {
        // all set up configuration
    }


    @Test
    void testFindAllByAccountNumber() {
        List<Transaction> transactionsByAccountId = transactionService.findAllByAccountNumber(12456789L);
        // // not null and not empty
        // // assert the size
        assertNotNull(transactionsByAccountId);
        assertFalse(transactionsByAccountId.isEmpty());
        assertEquals(3, transactionsByAccountId.size());

    }
    

}