package com.societegenerale.bankaccount.application;

import com.societegenerale.bankaccount.domain.Transaction;
import com.societegenerale.bankaccount.domain.TransactionType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StatementPrinter {

  private static final String STATEMENT_HEADER = "STATEMENT\n";
  private static final String ACCOUNT_ID_PREFIX = "Account ID: ";
  private static final String BALANCE_PREFIX = "Current Balance: ";
  private static final String TRANSACTION_HEADER = "DATE | AMOUNT | TRANS. TYPE";
  private static final String LINE_SEPARATOR = "----------------------";

  public void print(String accountId, List<Transaction> transactions, BigDecimal balance) {
    System.out.println(STATEMENT_HEADER);
    System.out.println(ACCOUNT_ID_PREFIX + accountId + "\n");
    System.out.println(TRANSACTION_HEADER);
    System.out.println(LINE_SEPARATOR);
    transactions.stream()
        .map(this::formatTransaction)
        .forEach(System.out::println);
    System.out.println();
    System.out.println(BALANCE_PREFIX + balance.setScale(2, RoundingMode.UP));
  }

  private String formatTransaction(Transaction transaction) {
    final StringBuilder builder = new StringBuilder();
    final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    final String formattedDate = transaction.getDate().format(dateFormatter);
    final String formattedAmount = formatAmount(transaction.getAmount(),
        transaction.getTransactionType().equals(
            TransactionType.DEPOSIT));

    builder.append(formattedDate).append(" | ");
    builder.append(formattedAmount).append(" | ");
    builder.append(transaction.getTransactionType());

    return builder.toString();
  }

  private String formatAmount(BigDecimal amount, Boolean positiveValue) {
    final BigDecimal scale = amount.setScale(2, RoundingMode.UP);
    return positiveValue ? scale.toString() : scale.negate().toString();
  }
}
