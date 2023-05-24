package com.societegenerale.bankaccount.api;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.societegenerale.bankaccount.application.BankAccountService;
import com.societegenerale.bankaccount.domain.Account;
import com.societegenerale.bankaccount.domain.BankAccountException;
import com.societegenerale.bankaccount.domain.Transaction;
import com.societegenerale.bankaccount.testUtils.AccountFixture;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class BankAccountControllerTest {

  private MockMvc mockMvc;

  @Mock
  private BankAccountService bankAccountService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    BankAccountController bankAccountController = new BankAccountController(bankAccountService);
    mockMvc = MockMvcBuilders.standaloneSetup(bankAccountController).build();
  }

  @Test
  public void createAccount_ShouldReturnCreatedStatusAndAccount() throws Exception {
    final Account account = new Account("1", Collections.emptyList());
    when(bankAccountService.createAccount()).thenReturn(account);

    mockMvc.perform(MockMvcRequestBuilders.post("/accounts"))
        .andExpect(status().isCreated())
        .andExpect(content().json("{'accountId': '1', 'transactions': []}"));

    verify(bankAccountService, times(1)).createAccount();
  }

  @Test
  public void deposit_ValidAccountId_ShouldReturnOkStatus() throws Exception {
    final String accountId = "1";
    BigDecimal amount = BigDecimal.valueOf(100);

    mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{accountId}/deposit", accountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(amount.toString()))
        .andExpect(status().isOk());

    verify(bankAccountService, times(1)).deposit(accountId, amount);
  }

  @Test
  public void deposit_InvalidAccountId_ShouldReturnNotFoundStatus() throws Exception {
    String invalidAccountId = "invalid";
    BigDecimal amount = BigDecimal.valueOf(100);

    doThrow(new BankAccountException("Account not found")).when(bankAccountService)
        .deposit(invalidAccountId, amount);

    mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{accountId}/deposit", invalidAccountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(amount.toString()))
        .andExpect(status().isNotFound());

    verify(bankAccountService, times(1)).deposit(invalidAccountId, amount);
  }

  @Test
  public void withdrawal_ValidAccountId_ShouldReturnOkStatus() throws Exception {
    String accountId = "1";
    BigDecimal amount = BigDecimal.valueOf(50);

    mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{accountId}/withdrawal", accountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(amount.toString()))
        .andExpect(status().isOk());

    verify(bankAccountService, times(1)).withdrawal(accountId, amount);
  }

  @Test
  public void withdrawal_InvalidAccountId_ShouldReturnNotFoundStatus() throws Exception {
    String invalidAccountId = "invalid";
    BigDecimal amount = BigDecimal.valueOf(50);

    doThrow(new BankAccountException("Account not found")).when(bankAccountService)
        .withdrawal(invalidAccountId, amount);

    mockMvc
        .perform(MockMvcRequestBuilders.post("/accounts/{accountId}/withdrawal", invalidAccountId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(amount.toString()))
        .andExpect(status().isNotFound());

    verify(bankAccountService, times(1)).withdrawal(invalidAccountId, amount);
  }

  @Test
  public void getBalance_ValidAccountId_ShouldReturnBalance() throws Exception {
    String accountId = "1";
    BigDecimal balance = BigDecimal.valueOf(200);

    when(bankAccountService.getBalance(accountId)).thenReturn(balance);

    mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{accountId}/balance", accountId))
        .andExpect(status().isOk())
        .andExpect(content().string(balance.toString()));

    verify(bankAccountService, times(1)).getBalance(accountId);
  }

  @Test
  public void getBalance_InvalidAccountId_ShouldReturnNotFoundStatus() throws Exception {
    String invalidAccountId = "invalid";

    when(bankAccountService.getBalance(invalidAccountId))
        .thenThrow(new BankAccountException("Account not found"));

    mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{accountId}/balance", invalidAccountId))
        .andExpect(status().isNotFound());

    verify(bankAccountService, times(1)).getBalance(invalidAccountId);
  }

  @Test
  public void getAccountTransactions_ValidAccountId_ShouldReturnTransactions() throws Exception {
    Account accountWithTransactions = AccountFixture.createAccountWithTransactions(2);
    Transaction transaction1 = accountWithTransactions.getTransactions().get(0);
    Transaction transaction2 = accountWithTransactions.getTransactions().get(1);
    String accountId = accountWithTransactions.getAccountId();

    when(bankAccountService.getAccount(accountId))
        .thenReturn(new Account(accountId, accountWithTransactions.getTransactions()));
    when(bankAccountService.getBalance(accountId)).thenReturn(accountWithTransactions.getBalance());

    mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{accountId}/transactions", accountId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(accountWithTransactions.getTransactions().size()))
        .andExpect(jsonPath("$[0].transactionType").value(transaction1.getTransactionType().name()))
        .andExpect(jsonPath("$[0].amount").value(transaction1.getAmount()))
        .andExpect(jsonPath("$[1].transactionType").value(transaction2.getTransactionType().name()))
        .andExpect(jsonPath("$[1].amount").value(transaction2.getAmount()));

    verify(bankAccountService, times(1)).getAccount(accountId);
  }

  @Test
  public void getAccountTransactions_InvalidAccountId_ShouldReturnNotFoundStatus()
      throws Exception {
    String invalidAccountId = "invalid";

    when(bankAccountService.getAccount(invalidAccountId))
        .thenThrow(new BankAccountException("Account not found"));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/accounts/{accountId}/transactions", invalidAccountId))
        .andExpect(status().isNotFound());

    verify(bankAccountService, times(1)).getAccount(invalidAccountId);
  }
}
