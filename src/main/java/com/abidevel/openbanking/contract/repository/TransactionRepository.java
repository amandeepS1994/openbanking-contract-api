package com.abidevel.openbanking.contract.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abidevel.openbanking.contract.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountNumber(Long accountNumber);
    @Query(value="select distinct (account_number) from transaction", nativeQuery=true)
    List<Long> findAllDistinct();
}
