package com.util.stmt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ValidatePrintStmtTest {

    @Test
    void testValidInput() {
        String[] validData = {"AB123", "202501"};
        boolean result = ValidatePrintStmt.isValid(validData);
        assertTrue(result);
    }

    @Test
    void testInvalidYearMonthFormat() {
        String[] invalidYearMonth = {"AB123", "202513"};
        boolean result = ValidatePrintStmt.isValid(invalidYearMonth);
        assertFalse(result);
    }

    @Test
    void testInvalidAccountFormat() {
        String[] invalidAccount = {"AB1235", "202501"};
        boolean result = ValidatePrintStmt.isValid(invalidAccount);
        assertFalse(result);
    }

    @Test
    void testNullYearMonth() {
        String[] nullYearMonth = {"AB123", null};
        boolean result = ValidatePrintStmt.isValid(nullYearMonth);
        assertFalse(result);
    }

    @Test
    void testNullAccount() {
        String[] nullAccount = {null, "202501"};
        boolean result = ValidatePrintStmt.isValid(nullAccount);
        assertFalse(result);
    }

    @Test
    void testNullYearMonthAndAccount() {
        String[] nullData = {null, null};
        boolean result = ValidatePrintStmt.isValid(nullData);
        assertFalse(result);
    }

    @Test
    void testInvalidYearMonthFormatPattern() {
        String[] invalidYearMonthFormat = {"AB123", "189913"};
        boolean result = ValidatePrintStmt.isValid(invalidYearMonthFormat);
        assertFalse(result);
    }


}
