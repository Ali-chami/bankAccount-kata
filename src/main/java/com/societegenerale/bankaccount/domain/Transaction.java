package com.societegenerale.bankaccount.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Transaction {

  private final TransactionType transactionType;
  private final LocalDateTime date;
  private final BigDecimal amount;
}
