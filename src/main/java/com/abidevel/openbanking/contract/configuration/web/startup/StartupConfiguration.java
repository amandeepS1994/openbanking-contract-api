package com.abidevel.openbanking.contract.configuration.web.startup;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.abidevel.openbanking.contract.model.Transaction;
import com.abidevel.openbanking.contract.model.enumeration.TransactionType;
import com.abidevel.openbanking.contract.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Profile("dev")
@Component
@Slf4j
public class StartupConfiguration implements ApplicationRunner {
    private final TransactionRepository transactionRepository;

    public StartupConfiguration(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        transactionRepository.saveAll(List.of(
            new Transaction(1l, TransactionType.CASH.name(), OffsetDateTime.now(), 1234567L, "£", 12345678.1, "Bookers", "merchantLogo.svg"),
            new Transaction(2l, TransactionType.CRYPTO.name(), OffsetDateTime.now(), 12345678L, "£", 12345678.1, "Crypto", "crypto.com.svg"),
            new Transaction(3l, TransactionType.CRYPTO.name(), OffsetDateTime.now(), 12345L, "£", 12345678.1, "Crypto", "crypto.com.svg"),
            new Transaction(4l, TransactionType.CHEQUE.name(), OffsetDateTime.now(), 123456L, "£", 12345678.1, "Taj", "Taj.svg")));
    }
    
}
