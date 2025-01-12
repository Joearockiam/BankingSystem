package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import com.model.Account;
import com.model.AccountTransaction;
import com.repository.AccountRepository;
import com.repository.AccountTransactionRepository;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InputTransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountTransactionRepository accountTransactionRepository;

    @InjectMocks
    private InputTransactionService inputTransactionService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .accountNo("12345")
                .balance(1000.0)
                .build();
    }

    @Test
    @Order(1)
    @Description("This is to test if the account already there and the current transaction is 'W'")
    void testIsValidTransactionValidWithdrawal() {
        when(accountRepository.findById("12345")).thenReturn(Optional.of(account));

        boolean result = inputTransactionService.isValidTransaction("12345", "w");

        assertTrue(result);
        verify(accountRepository, times(1)).findById("12345");
    }

    @Test
    @Order(2)
    @Description("This is to test if the account already not there and the current transaction is 'W'")
    void testIsValidTransactionInvalidWithdrawal() {
        when(accountRepository.findById("12345")).thenReturn(Optional.empty());

        boolean result = inputTransactionService.isValidTransaction("12345", "w");

        assertFalse(result);
        verify(accountRepository, times(1)).findById("12345");
    }
    
    
    @Test
    @Order(3)
    @Description("This is to test the new account with deposit ('D')")
    void testProcessNewAccountDeposit() {
        when(accountRepository.findById("67890")).thenReturn(Optional.empty());

        boolean result = inputTransactionService.process("20250112", "67890", "d", 500.0);

        assertTrue(result);
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    
    
    @Test
    @Order(4)
    @Description("This is to test the existing account with deposit ('D')")
    void testProcessExistingAccountDeposit() {
        when(accountRepository.findById("12345")).thenReturn(Optional.of(account));

        boolean result = inputTransactionService.process("20250112", "12345", "d", 500.0);

        assertTrue(result);
        assertEquals(1500.0, account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }
    
    
    @Test
    @Order(5)
    @Description("This is to test the get balance for the exising account")
    void testGetBalanceExistingAccount() {
        when(accountRepository.findById("12345")).thenReturn(Optional.of(account));

        double balance = inputTransactionService.getBalance("12345");

        assertEquals(1000.0, balance);
        verify(accountRepository, times(1)).findById("12345");
    }
    
    
    @Test
    @Order(6)
    @Description("This is to test the get balance for the non exising account")
    void testGetBalanceNonExistingAccount() {
        when(accountRepository.findById("23456")).thenReturn(Optional.empty());

        double balance = inputTransactionService.getBalance("23456");

        assertEquals(0.0, balance);
        verify(accountRepository, times(1)).findById("23456");
    }
    
    
    @Test
    @Order(7)
    @Description("This is to test the get number of transactions")
    void testGetNumberOfTransactions() {
        when(accountTransactionRepository.findByAccount_AccountNoAndTxnDateAndTxnDateIsNotNullOrderByUpdatedDateDesc("12345", "20250112"))
                .thenReturn(List.of(new AccountTransaction(), new AccountTransaction()));

        int transactionCount = inputTransactionService.getNumberofTransaction("12345", "20250112");

        assertEquals(2, transactionCount);
        verify(accountTransactionRepository, times(1)).findByAccount_AccountNoAndTxnDateAndTxnDateIsNotNullOrderByUpdatedDateDesc("12345", "20250112");
    }
    
    
    @Test
    @Order(8)
    @Description("This is to test the retrive all transactions")
    void testRetrieveAllTransactions() {
        List<AccountTransaction> transactions = List.of(new AccountTransaction(), new AccountTransaction());
        when(accountTransactionRepository.findByAccount_AccountNoAndTxnDateAndTxnDateIsNotNullOrderByUpdatedDateDesc("12345", "20250112"))
                .thenReturn(transactions);

        List<AccountTransaction> result = inputTransactionService.retrieveAllTransaction("12345", "20250112");

        assertEquals(2, result.size());
        verify(accountTransactionRepository, times(1)).findByAccount_AccountNoAndTxnDateAndTxnDateIsNotNullOrderByUpdatedDateDesc("12345", "20250112");
    }
    
}