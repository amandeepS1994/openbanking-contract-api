package com.abidevel.openbanking.contract.model.adapter;

import com.abidevel.openbanking.banking.model.OBCreditDebitCode1;
import com.abidevel.openbanking.banking.model.OBTransaction6;
import com.abidevel.openbanking.contract.model.Transaction;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.Objects;

@Data
@Slf4j
public class ObTransactionAdapter {
    private final OBTransaction6 OBtransaction;
    private final Transaction transaction;
    private boolean isSuccessful;

    public ObTransactionAdapter (OBTransaction6 transaction) {
        this.OBtransaction = transaction;
        this.transaction = configureTransaction(transaction);
    }

    private Transaction configureTransaction(OBTransaction6 obTransaction) {
        this.isSuccessful = validateValues(obTransaction);
        return isSuccessful ? Transaction.builder()
                .accountNumber(Long.parseLong(obTransaction.getAccountId()))
                .type(obTransaction.getCreditDebitIndicator().toString())
                .currency(obTransaction.getCurrencyExchange().getUnitCurrency())
                .amount(null)
                .merchantName(obTransaction.getMerchantDetails().getMerchantName())
                .date(obTransaction.getValueDateTime())
                .build() : null;
    }

    private boolean validateValues (OBTransaction6 obTransaction6) {
        return Objects.nonNull(obTransaction6) && Objects.nonNull(obTransaction6.getAccountId()) && Objects.nonNull(obTransaction6.getCreditDebitIndicator())
                && Objects.nonNull(obTransaction6.getCurrencyExchange()) && Objects.nonNull(obTransaction6.getAmount()) && Objects.nonNull(obTransaction6.getMerchantDetails())
                && Objects.nonNull(obTransaction6.getValueDateTime());
    }


}
