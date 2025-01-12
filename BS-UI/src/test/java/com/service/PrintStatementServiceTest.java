package com.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;


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
public class PrintStatementServiceTest {

    @Mock
    private AccountTransactionRepository accTransRepository;

    @Mock
    private AccountRepository accRepo;

    @InjectMocks
    private PrintStatementService printStatementService;

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
    @Description("To test the RetrieveAllTransactionValidAccount")
    void testRetrieveAllTransactionValidAccount() {
        String accountNo = "12345";
        String monthYear = "202501";
        List<AccountTransaction> transactions = List.of(new AccountTransaction(), new AccountTransaction());

        when(accTransRepository.findByAccountNoYearMonth(accountNo, monthYear)).thenReturn(transactions);

        List<AccountTransaction> result = printStatementService.retrieveAllTransaction(accountNo, monthYear);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(accTransRepository, times(1)).findByAccountNoYearMonth(accountNo, monthYear);
    }

    @Test
    @Order(2)
    @Description("To test the RetrieveAllTransaction for the existing acc. But, for the month ad year no transaction")
    void testRetrieveAllTransactionNoTransactions() {
        String accountNo = "12345";
        String monthYear = "202501";

        when(accTransRepository.findByAccountNoYearMonth(accountNo, monthYear)).thenReturn(List.of());

        List<AccountTransaction> result = printStatementService.retrieveAllTransaction(accountNo, monthYear);

        //assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(accTransRepository, times(1)).findByAccountNoYearMonth(accountNo, monthYear);
    }

    @Test
    @Order(3)
    @Description("To test the existing account")
    void testIsAccountExistAccountExists() {
        when(accRepo.findById("12345")).thenReturn(Optional.of(account));

        boolean result = printStatementService.isAccountExist("12345");

        assertTrue(result);
        verify(accRepo, times(1)).findById("12345");
    }

    @Test
    @Order(4)
    @Description("To test the non existing account")
    void testIsAccountExistAccountDoesNotExist() {
        when(accRepo.findById("67890")).thenReturn(Optional.empty());

        boolean result = printStatementService.isAccountExist("67890");

        assertFalse(result);
        verify(accRepo, times(1)).findById("67890");
    }
}
