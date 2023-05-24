package com.societegenerale.bankaccount.api;

import com.societegenerale.bankaccount.application.BankAccountService;
import com.societegenerale.bankaccount.application.StatementPrinter;
import com.societegenerale.bankaccount.domain.Account;
import com.societegenerale.bankaccount.domain.BankAccountException;
import com.societegenerale.bankaccount.domain.Transaction;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

  private final BankAccountService bankAccountService;

  @Autowired
  public BankAccountController(BankAccountService bankAccountService) {
    this.bankAccountService = bankAccountService;
  }

  @PostMapping
  public ResponseEntity<Account> createAccount() {
    Account account = bankAccountService.createAccount();
    return ResponseEntity.status(HttpStatus.CREATED).body(account);
  }

  @PostMapping("/{accountId}/deposit")
  public ResponseEntity<Void> deposit(@PathVariable String accountId,
      @RequestBody BigDecimal amount) {
    bankAccountService.deposit(accountId, amount);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{accountId}/withdrawal")
  public ResponseEntity<Void> withdraw(@PathVariable String accountId,
      @RequestBody BigDecimal amount) {
    bankAccountService.withdrawal(accountId, amount);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{accountId}/balance")
  public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountId) {
    BigDecimal balance = bankAccountService.getBalance(accountId);
    return ResponseEntity.ok(balance);
  }

  @GetMapping("/{accountId}/transactions")
  public ResponseEntity<List<Transaction>> getAccountTransactions(@PathVariable String accountId) {
    try {
      List<Transaction> transactions = bankAccountService.getAccount(accountId).getTransactions();
      StatementPrinter printer = new StatementPrinter();
      printer.print(accountId, transactions, bankAccountService.getBalance(accountId));
      return ResponseEntity.ok(transactions);
    } catch (BankAccountException exception) {
      List<Transaction> emptyList = Collections.emptyList();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList);
    }
  }

  @ExceptionHandler(BankAccountException.class)
  public ResponseEntity<List<Transaction>> handleBankAccountException(BankAccountException ex) {
    List<Transaction> emptyList = Collections.emptyList();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList);
  }

}
