package com.abidevel.openbanking.contract.model;

import java.util.Date;

import com.abidevel.openbanking.contract.model.enumeration.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private Long id;
    private TransactionType type;
    private Date date;
    private Long accountNumber;
    private String currency;
    private Long amount;
    private String merchantName;
    private String merchantLogo;
}
