package com.abidevel.openbanking.contract.adapter;

import com.abidevel.openbanking.banking.model.OBActiveOrHistoricCurrencyAndAmount9;
import com.abidevel.openbanking.banking.model.OBCreditDebitCode1;
import com.abidevel.openbanking.banking.model.OBCurrencyExchange5;
import com.abidevel.openbanking.banking.model.OBMerchantDetails1;
import com.abidevel.openbanking.banking.model.OBTransaction6;
import com.abidevel.openbanking.contract.ContractApplicationTests;
import com.abidevel.openbanking.contract.model.adapter.ObTransactionAdapter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ObTransactionAdapterTest extends ContractApplicationTests {
    private static OBTransaction6 successTransaction;
    private static OBTransaction6 failedTransaction;
    private static ObTransactionAdapter obTransactionAdapterSuccess;
    private static ObTransactionAdapter obTransactionAdapterFailed;

    @BeforeAll
    static void configureTests () {
        successTransaction = new OBTransaction6();
        successTransaction.setAccountId("1");
        successTransaction.setCreditDebitIndicator(OBCreditDebitCode1.CREDIT);
        successTransaction.setCurrencyExchange(new OBCurrencyExchange5());
        successTransaction.setAmount(new OBActiveOrHistoricCurrencyAndAmount9());
        successTransaction.setMerchantDetails(new OBMerchantDetails1());
        successTransaction.setValueDateTime(OffsetDateTime.now());

        failedTransaction = new OBTransaction6();
        failedTransaction.setAccountId("2");
        failedTransaction.setCreditDebitIndicator(OBCreditDebitCode1.DEBIT);
        failedTransaction.setCurrencyExchange(null);
        failedTransaction.setAmount(null);
        failedTransaction.setMerchantDetails(null);
        failedTransaction.setValueDateTime(OffsetDateTime.now());

        obTransactionAdapterSuccess = new ObTransactionAdapter(successTransaction);
        obTransactionAdapterFailed = new ObTransactionAdapter(failedTransaction);

    }

    @Test
    @DisplayName(value = "Given an Open-Banking Transaction Model passed into Open-Banking Transaction Adapter, " +
            "assert that the transaction is model is attained.")
    void testObTransactionAdapter() {
        assertTrue(obTransactionAdapterSuccess.isSuccessful());
        assertNotNull(obTransactionAdapterSuccess.getTransaction());
        assertEquals(Long.parseLong("1"), obTransactionAdapterSuccess.getTransaction().getAccountNumber());
        assertEquals(successTransaction.getValueDateTime(), obTransactionAdapterSuccess.getTransaction().getDate());
    }

    @Test
    @DisplayName(value = "Given an Open-Banking Transaction Model passed (invalid state) into Open-Banking Transaction Adapter," +
            "assert that the transaction is model is failed.")
    void testObTransactionAdapteFailed() {
        assertFalse(obTransactionAdapterFailed.isSuccessful());
        assertNull(obTransactionAdapterFailed.getTransaction());
    }
}
