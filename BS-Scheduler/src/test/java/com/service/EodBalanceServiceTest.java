package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.common.CommonUtil;
import com.model.EodBalance;
import com.model.EodId;
import com.repository.AccTransactionRepository;
import com.repository.EodBalanceRepository;
import com.repository.RuleRepository;

@ExtendWith(MockitoExtension.class)
class EodBalanceServiceTest {

    @InjectMocks
    private EodBalanceService eodBalanceService;

    @Mock
    private AccTransactionRepository accTransRepo;

    @Mock
    private EodBalanceRepository eodRepo;

    @Mock
    private RuleRepository ruleRepo;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        // Start the static mock before each test
        closeable = mockStatic(CommonUtil.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Deregister the static mock after each test
        closeable.close();
    }
    
    @Test
    void testProcessEodBalanceSuccess() {
        //prepare
        List<Object[]> transactions = new ArrayList<>();
        transactions.add(new Object[]{"12345", 1000.0});
        when(accTransRepo.computeEodForAllAccount()).thenReturn(transactions);

        List<Object[]> rules = new ArrayList<>();
        rules.add(new Object[]{"20250112", "Rule01", 5.0});
        when(ruleRepo.getLatestRule()).thenReturn(rules);

        when(eodRepo.saveAll(anyList())).thenReturn(null);

        boolean result = eodBalanceService.processEodBalance();

        assertTrue(result);
        verify(accTransRepo, times(1)).computeEodForAllAccount();
        verify(ruleRepo, times(1)).getLatestRule();
        verify(eodRepo, times(1)).saveAll(anyList());
    }

    @Test
    void testProcessEodBalanceFailure() {
        // prepare
        when(accTransRepo.computeEodForAllAccount()).thenThrow(new RuntimeException("Database error"));

        boolean result = eodBalanceService.processEodBalance();

        assertFalse(result);
        verify(accTransRepo, times(1)).computeEodForAllAccount();
        verify(ruleRepo, times(0)).getLatestRule();
        verify(eodRepo, times(0)).saveAll(anyList());
    }

    @Test
    void testIsEodProcessedOnce() {
        // prepare
        String currentDateStr = CommonUtil.getCurrentDateBasicISO();
        when(eodRepo.countDistinctEodDate(currentDateStr)).thenReturn(1);

        boolean result = eodBalanceService.isEodProcessed(currentDateStr);

        assertTrue(result);
        verify(eodRepo, times(1)).countDistinctEodDate(currentDateStr);
    }

    @Test
    void testIsEodProcessedZero() {
        // prepare
        String currentDateStr = CommonUtil.getCurrentDateBasicISO();
        when(eodRepo.countDistinctEodDate(currentDateStr)).thenReturn(0);

        boolean result = eodBalanceService.isEodProcessed(currentDateStr);

        assertFalse(result);
        verify(eodRepo, times(1)).countDistinctEodDate(currentDateStr);
    }
}
