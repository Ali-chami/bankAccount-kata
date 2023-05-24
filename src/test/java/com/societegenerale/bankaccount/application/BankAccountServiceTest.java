package com.societegenerale.bankaccount.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.societegenerale.bankaccount.domain.Account;
import com.societegenerale.bankaccount.domain.BankAccountException;
import com.societegenerale.bankaccount.domain.TransactionType;
import com.societegenerale.bankaccount.testUtils.AccountFixture;
import java.math.BigDecimal;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankAccountServiceTest {

  private BankAccountService bankAccountService;
  private Account account;
  private String accountId;

  @BeforeEach
  public void setup() {
    account = AccountFixture.createAccountWithTransactions(5, TransactionType.DEPOSIT);
    accountId = account.getAccountId();
    bankAccountService = new BankAccountService(Maps.newHashMap(account.getAccountId(), account));
  }

  @Test
  public void testCreateAccount() {
    final Account createdAccount = bankAccountService.createAccount();

    assertNotNull(createdAccount);
    assertEquals(0, createdAccount.getTransactions().size());
  }

  @Test
  public void testDeposit() {
    final BigDecimal amount = BigDecimal.valueOf(100);

    bankAccountService.deposit(accountId, amount);

    assertEquals(account.getBalance().add(amount), bankAccountService.getBalance(accountId));
    assertEquals(account.getTransactions().size() + 1,
        bankAccountService.getAccount(accountId).getTransactions().size());
  }

  @Test
  public void testWithdrawSufficientBalance() {
    final BigDecimal amount = BigDecimal.valueOf(50);

    bankAccountService.withdrawal(accountId, amount);
    account = account.withdrawal(amount);

    assertEquals(account.getBalance(), bankAccountService.getBalance(accountId));
  }

  @Test
  public void testWithdrawInsufficientBalance() {
    final BigDecimal amount = account.getBalance().add(BigDecimal.valueOf(100));

    assertThrows(BankAccountException.class,
        () -> bankAccountService.withdrawal(account.getAccountId(), amount));
  }

  @Test
  public void testGetBalance() {
    final BigDecimal balance = bankAccountService.getBalance(account.getAccountId());
    assertEquals(account.getBalance(), balance);
  }

  @Test
  public void testMultipleDepositsAndWithdrawals() {
    final BigDecimal depositAmount = BigDecimal.valueOf(100);
    final BigDecimal withdrawalAmount = BigDecimal.valueOf(50);

    bankAccountService.deposit(accountId, depositAmount);
    account = account.deposit(depositAmount);
    assertEquals(account.getBalance(), bankAccountService.getBalance(accountId));

    bankAccountService.withdrawal(accountId, withdrawalAmount);
    account = account.withdrawal(withdrawalAmount);
    assertEquals(account.getBalance(), bankAccountService.getBalance(accountId));

    bankAccountService.deposit(accountId, depositAmount);
    account = account.deposit(depositAmount);
    assertEquals(account.getBalance(), bankAccountService.getBalance(accountId));

    bankAccountService.withdrawal(accountId, withdrawalAmount);
    account = account.withdrawal(withdrawalAmount);
    assertEquals(account.getBalance(), bankAccountService.getBalance(accountId));
  }

  @Test
  public void testDepositWithInvalidAccountId() {
    final String invalidAccountId = "invalidAccountId";
    final BigDecimal amount = BigDecimal.valueOf(100);

    assertThrows(BankAccountException.class,
        () -> bankAccountService.deposit(invalidAccountId, amount));
  }

  @Test
  public void testWithdrawWithInvalidAccountId() {
    final String invalidAccountId = "invalidAccountId";
    final BigDecimal amount = BigDecimal.valueOf(100);

    assertThrows(BankAccountException.class,
        () -> bankAccountService.withdrawal(invalidAccountId, amount));
  }

  @Test
  public void testGetBalanceWithInvalidAccountId() {
    final String invalidAccountId = "invalidAccountId";

    assertThrows(BankAccountException.class, () -> bankAccountService.getBalance(invalidAccountId));
  }
}
