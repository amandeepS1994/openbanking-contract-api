package com.abidevel.openbanking.contract.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abidevel.openbanking.contract.model.Response.ApiResponse;
import com.abidevel.openbanking.contract.service.TransactionService;

@RestController
@RequestMapping("transactions/")
public class TransactionController {
    private final TransactionService transactionService;
    
    public TransactionController (TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(path = "{accountNumber}/")
    @PreAuthorize("hasRole('ROLE_BANKING-USER')")
    public ResponseEntity<ApiResponse<Object>> retrieveAllTransactionForAccountNumber(@PathVariable Long accountNumber) {
        return new ResponseEntity<>(ApiResponse.builder()
        .isSuccessful(true)
        .apireponse(new Date())
        .data(transactionService.findAllByAccountNumber(accountNumber))
        .build(),
        HttpStatus.OK
        );
    }

}
