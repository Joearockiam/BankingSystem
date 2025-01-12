package com.scheduler;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.common.CommonUtil;
import com.service.EodBalanceService;
import com.service.EomInterestService;

@ExtendWith(MockitoExtension.class)
class SchedulerTest {

    @Mock
    private EodBalanceService eodService;

    @Mock
    private EomInterestService eomService;

    @InjectMocks
    private Scheduler scheduler;

    AutoCloseable closeable;
    
    @BeforeEach
    void setUp() {
        // Start the static mock before each test
        closeable = mockStatic(CommonUtil.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testRunEodTaskEodAlreadyProcessed() {
        when(CommonUtil.getCurrentDateBasicISO()).thenReturn("20250112");
        when(eodService.isEodProcessed("20250112")).thenReturn(true);

        scheduler.runEodTask();

        verify(eodService, times(1)).isEodProcessed("20250112");
        verify(eodService, never()).processEodBalance();
    }

    @Test
    void testRunEodTaskEodNotProcessed() {
        when(CommonUtil.getCurrentDateBasicISO()).thenReturn("20250112");
        when(eodService.isEodProcessed("20250112")).thenReturn(false);
        when(eodService.processEodBalance()).thenReturn(true);

        scheduler.runEodTask();

        verify(eodService, times(1)).isEodProcessed("20250112");
        verify(eodService, times(1)).processEodBalance();
    }

    @Test
    void testRunEomTaskEomAlreadyProcessed() {
        when(CommonUtil.getCurrentDateBasicISO()).thenReturn("20250112");
        when(eomService.isEomProcessed("20250112", "I")).thenReturn(true);

        scheduler.runEomTask();

        verify(eomService, times(1)).isEomProcessed("20250112", "I");
        verify(eomService, never()).calculateEomInterest();
    }

    @Test
    void testRunEomTaskEomNotProcessed() {
        when(CommonUtil.getCurrentDateBasicISO()).thenReturn("20250112");
        when(eomService.isEomProcessed("20250112", "I")).thenReturn(false);
        when(eomService.calculateEomInterest()).thenReturn(true);

        scheduler.runEomTask();

        verify(eomService, times(1)).isEomProcessed("20250112", "I");
        verify(eomService, times(1)).calculateEomInterest();
    }
}
