package com.societegenerale.bankaccount.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Account {

  private final String accountId;
  private final List<Transaction> transactions;

  public Account() {
    this.accountId = UUID.randomUUID().toString();
    this.transactions = new ArrayList<>();
  }

  public Account deposit(BigDecimal amount) {
    final Transaction depositTransaction = new Transaction(TransactionType.DEPOSIT,
        LocalDateTime.now(),
        amount);

    final List<Transaction> updatedTransactions = Stream
        .concat(transactions.stream(), Stream.of(depositTransaction))
        .collect(Collectors.toList());

    return new Account(accountId, updatedTransactions);
  }

  public Account withdrawal(BigDecimal amount) {
    if (this.getBalance().compareTo(amount) < 0) {
      throw new BankAccountException("Insufficient balance");
    }

    final Transaction withdrawalTransaction = new Transaction(TransactionType.WITHDRAWAL,
        LocalDateTime.now(), amount.negate());

    final List<Transaction> updatedTransactions = Stream
        .concat(transactions.stream(), Stream.of(withdrawalTransaction))
        .collect(Collectors.toList());

    return new Account(accountId, updatedTransactions);
  }

  public BigDecimal getBalance() {
    return transactions.stream()
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
