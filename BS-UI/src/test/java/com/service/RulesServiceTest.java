package com.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
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
import org.mockito.junit.jupiter.MockitoExtension;

import com.model.Rule;
import com.repository.RuleRepository;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RulesServiceTest {

    @Mock
    private RuleRepository ruleRepository;

    @InjectMocks
    private RulesService rulesService;

    private Rule rule;

    @BeforeEach
    void setUp() {
        rule = Rule.builder()
                .ruleDate("20250101")
                .ruleId("R001")
                .rate(1.5)
                .build();
    }

    @Test
    @Order(1)
    void testCreateUpdateRuleNewRule() {
        String dateStr = "20250101";
        String ruleId = "R001";
        double rate = 1.5;

        when(ruleRepository.findById(dateStr)).thenReturn(Optional.empty());
        when(ruleRepository.save(any(Rule.class))).thenReturn(rule);

        boolean result = rulesService.createUpdateRule(dateStr, ruleId, rate);

        assertTrue(result);
        verify(ruleRepository, times(1)).findById(dateStr);
        verify(ruleRepository, times(1)).save(any(Rule.class));
    }

    @Test
    @Order(2)
    void testCreateUpdateRuleUpdateExistingRule() {
        String dateStr = "20250101";
        String ruleId = "R002";
        double rate = 2.0;

        when(ruleRepository.findById(dateStr)).thenReturn(Optional.of(rule));
        when(ruleRepository.save(any(Rule.class))).thenReturn(rule);

        boolean result = rulesService.createUpdateRule(dateStr, ruleId, rate);

        assertTrue(result);
        assertEquals("R002", rule.getRuleId());
        assertEquals(2.0, rule.getRate());
        verify(ruleRepository, times(1)).findById(dateStr);
        verify(ruleRepository, times(1)).save(rule);
    }

    @Test
    @Order(3)
    void testCreateUpdateRuleException() {
        String dateStr = "20250101";
        String ruleId = "R003";
        double rate = 3.0;

        when(ruleRepository.findById(dateStr)).thenThrow(new RuntimeException("Database error"));

        boolean result = rulesService.createUpdateRule(dateStr, ruleId, rate);

        assertFalse(result);
        verify(ruleRepository, times(1)).findById(dateStr);
        verify(ruleRepository, never()).save(any(Rule.class));
    }

    @Test
    @Order(4)
    void testRetrieveAllRules() {
        List<Rule> rules = List.of(
                Rule.builder().ruleDate("20250101").ruleId("R001").rate(1.5).build(),
                Rule.builder().ruleDate("20250102").ruleId("R002").rate(2.5).build()
        );

        when(ruleRepository.findAll()).thenReturn(rules);

        List<Rule> result = rulesService.retriveAllRules();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ruleRepository, times(1)).findAll();
    }
}