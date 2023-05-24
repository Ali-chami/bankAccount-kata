package com.societegenerale.bankaccount.application;

import com.societegenerale.bankaccount.domain.Account;
import com.societegenerale.bankaccount.domain.BankAccountException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountService {

  private final Map<String, Account> accounts;

  public BankAccountService() {
    this.accounts = new HashMap<>();
  }

  public Account createAccount() {
    final Account account = new Account();
    accounts.put(account.getAccountId(), account);
    return account;
  }

  public void deposit(String accountId, BigDecimal amount) {
    final Account account = getAccount(accountId);
    final Account updatedAccount = account.deposit(amount);
    accounts.put(accountId, updatedAccount);
  }

  public void withdrawal(String accountId, BigDecimal amount) {
    final Account account = getAccount(accountId);
    final Account updatedAccount = account.withdrawal(amount);
    accounts.put(accountId, updatedAccount);
  }

  public BigDecimal getBalance(String accountId) {
    final Account account = getAccount(accountId);
    return account.getBalance();
  }

  public Account getAccount(String accountId) {
    final Account account = accounts.get(accountId);
    if (account == null) {
      throw new BankAccountException("Account not found");
    }
    return account;
  }
}
