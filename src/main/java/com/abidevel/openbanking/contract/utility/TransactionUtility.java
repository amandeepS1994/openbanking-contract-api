package com.abidevel.openbanking.contract.utility;

import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.abidevel.openbanking.contract.model.enumeration.TransactionType;

import io.vavr.collection.Stream;

public class TransactionUtility {
    private static final Random random;
    static {
        random = new Random();
    }

    private TransactionUtility () {

    }

    public static TransactionType retrieveTransactionType () {
        int selectedValue = random.nextInt(TransactionType.values().length);
        return Stream.of(TransactionType.values())
            .filter(t -> t.ordinal() == selectedValue)
            .getOrElse(() -> TransactionType.PAYPAL);
    }
}
