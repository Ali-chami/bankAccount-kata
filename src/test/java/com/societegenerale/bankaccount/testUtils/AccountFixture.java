package com.societegenerale.bankaccount.testUtils;

import com.github.javafaker.Faker;
import com.societegenerale.bankaccount.domain.Account;
import com.societegenerale.bankaccount.domain.Transaction;
import com.societegenerale.bankaccount.domain.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountFixture {

  private static final Faker faker = new Faker(new Locale("fr"));

  public static Account createAccountWithTransactions(int numTransactions) {
    final TransactionType[] transactionTypes = TransactionType.values();
    final TransactionType transactionType = getRandomTransactionType(transactionTypes);
    final List<Transaction> transactions = generateTransactions(numTransactions, transactionType);
    return new Account(generateAccountId(), transactions);
  }

  public static Account createAccountWithTransactions(int numTransactions,
      TransactionType transactionType) {
    final List<Transaction> transactions = generateTransactions(numTransactions, transactionType);
    return new Account(generateAccountId(), transactions);
  }

  private static List<Transaction> generateTransactions(int numTransactions,
      TransactionType transactionType) {
    return Stream.generate(() -> generateRandomTransaction(transactionType))
        .limit(numTransactions)
        .collect(Collectors.toList());
  }

  private static Transaction generateRandomTransaction(TransactionType transactionType) {
    final LocalDateTime timestamp = faker.date().past(30, TimeUnit.DAYS).toInstant()
        .atZone(ZoneId.systemDefault()).toLocalDateTime();
    final BigDecimal amount = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 1000));
    return new Transaction(transactionType, timestamp, amount);
  }

  private static TransactionType getRandomTransactionType(TransactionType[] transactionTypes) {
    return transactionTypes[faker.number().numberBetween(0, transactionTypes.length)];
  }

  private static String generateAccountId() {
    return UUID.randomUUID().toString();
  }

}
