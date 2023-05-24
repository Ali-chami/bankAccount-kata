package com.societegenerale.bankaccount.domain;

public class BankAccountException extends RuntimeException {

  public BankAccountException(String message) {
    super(message);
  }

  public BankAccountException(String message, Throwable cause) {
    super(message, cause);
  }
}
