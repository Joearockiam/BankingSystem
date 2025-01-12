package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.common.CommonUtil;
import com.model.Account;
import com.model.EodBalance;
import com.model.EodId;
import com.repository.AccTransactionRepository;
import com.repository.AccountRepository;
import com.repository.EodBalanceRepository;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EomInterestServiceTest {

	@InjectMocks
	private EomInterestService eomInterestService;

	@Mock
	private EodBalanceRepository eodRepo;

	@Mock
	private AccountRepository acctRepo;

	@Mock
	private AccTransactionRepository accTransRepo;

	
    AutoCloseable closeable;
    
	@BeforeEach
	void setUp() throws Exception {
		closeable = mockStatic(CommonUtil.class);
	}

	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}
	
	@Test
	@Order(1)
	void testCalculateEomInterestSuccess() {
		String firstDayOfMonth = "20250101";
        String lastDayOfMonth = "20250131";
        String currentDate = "20250112";
        
        when(CommonUtil.getFirstDayOfCurrMonthBasicISO()).thenReturn(firstDayOfMonth);
        when(CommonUtil.getLastDayOfCurrMonthBasicISO()).thenReturn(lastDayOfMonth);
        when(CommonUtil.getCurrentDateBasicISO()).thenReturn(currentDate);
        
        // Mock data
        List<Object[]> interestList = new ArrayList<>();
        interestList.add(new Object[]{"ACC001", 50.0});
        when(eodRepo.computeInterest(firstDayOfMonth, lastDayOfMonth)).thenReturn(interestList);

        Account account = new Account();
        account.setAccountNo("ACC001");
        account.setBalance(1000.0);
        when(acctRepo.findById("ACC001")).thenReturn(Optional.of(account));

        EodId eodId = EodId.builder().accountNo("ACC001").eodDate(currentDate).build();
        EodBalance eodBalance = new EodBalance();
        eodBalance.setBalance(1000.0);
        when(eodRepo.findById(eodId)).thenReturn(Optional.of(eodBalance));

        boolean result = eomInterestService.calculateEomInterest();

        assertTrue(result);
        verify(acctRepo, times(1)).saveAll(anyList());
        verify(eodRepo, times(1)).saveAll(anyList());
	}
	
	@Test
	@Order(2)
    void testCalculateEomInterestErrorHandling() {
        when(CommonUtil.getFirstDayOfCurrMonthBasicISO()).thenReturn("20250101");
        when(CommonUtil.getLastDayOfCurrMonthBasicISO()).thenReturn("20250131");

        when(eodRepo.computeInterest(anyString(), anyString())).thenThrow(new RuntimeException("Test Exception"));

        boolean result = eomInterestService.calculateEomInterest();

        assertFalse(result);
        verify(acctRepo, never()).saveAll(anyList());
        verify(eodRepo, never()).saveAll(anyList());
		}
	
	@Test
	@Order(3)
    void testIsEomProcessedTrue() {
        when(accTransRepo.findTopByDateAndTxnType("20250112", "I")).thenReturn(1);

        boolean result = eomInterestService.isEomProcessed("20250112", "I");

        assertTrue(result);
    }
	
	@Test
	@Order(4)
    void testIsEomProcessedFalse() {
        when(accTransRepo.findTopByDateAndTxnType("20250112", "I")).thenReturn(0);

        boolean result = eomInterestService.isEomProcessed("20250112", "I");

        assertFalse(result);
    }
}
