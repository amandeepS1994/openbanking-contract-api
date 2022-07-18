package com.abidevel.openbanking.contract.model.Response;

import java.util.List;

import com.abidevel.openbanking.banking.model.OBTransaction6;

import lombok.Data;

@Data
public class OpenBankingAccessTransactionWrapperResponse {
    private List<OBTransaction6> transactions;
}

