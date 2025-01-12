package com.util.input;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.common.CommonUtil;

import java.lang.reflect.Method;

import java.time.LocalDate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VaidateInputTransTest {

    @Test
    @Order(1)
    void testIsValidValidInput() {
        String[] validInput = {LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE), "AB123", "d", "100.00"};

        try (MockedStatic<CommonUtil> commonUtilMock = Mockito.mockStatic(CommonUtil.class)) {
            commonUtilMock.when(CommonUtil::getCurrentDateBasicISO).thenReturn(validInput[0]);
            assertTrue(VaidateInputTrans.isValid(validInput), "Valid input should return true.");
        }
    }

    @Test
    @Order(2)
    void testIsValidInvalidDate() {
        String[] invalidDateInput = {"20250101", "AB123", "d", "100.00"};
        try (MockedStatic<CommonUtil> commonUtilMock = Mockito.mockStatic(CommonUtil.class)) {
            commonUtilMock.when(CommonUtil::getCurrentDateBasicISO).thenReturn("20250112");
            assertFalse(VaidateInputTrans.isValid(invalidDateInput), "Invalid date should return false.");
        }
    }

    @Test
    @Order(3)
    void testIsValidInvalidAmount() {
        String[] invalidAmountInput = {LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE), "AB123", "d", "0.00"};
        try (MockedStatic<CommonUtil> commonUtilMock = Mockito.mockStatic(CommonUtil.class)) {
            commonUtilMock.when(CommonUtil::getCurrentDateBasicISO).thenReturn(invalidAmountInput[0]);
            assertFalse(VaidateInputTrans.isValid(invalidAmountInput), "Invalid amount should return false.");
        }
    }

    @Test
    @Order(4)
    void testIsValidInvalidTransactionType() {
        String[] invalidTxnTypeInput = {LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE), "AB123", "x", "100.00"};
        try (MockedStatic<CommonUtil> commonUtilMock = Mockito.mockStatic(CommonUtil.class)) {
            commonUtilMock.when(CommonUtil::getCurrentDateBasicISO).thenReturn(invalidTxnTypeInput[0]);
            assertFalse(VaidateInputTrans.isValid(invalidTxnTypeInput), "Invalid transaction type should return false.");
        }
    }

    @Test
    @Order(5)
    void testIsValidInvalidAccount() {
        String[] invalidAccountInput = {LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE), "12345", "d", "100.00"};
        try (MockedStatic<CommonUtil> commonUtilMock = Mockito.mockStatic(CommonUtil.class)) {
            commonUtilMock.when(CommonUtil::getCurrentDateBasicISO).thenReturn(invalidAccountInput[0]);
            assertFalse(VaidateInputTrans.isValid(invalidAccountInput), "Invalid account should return false.");
        }
    }

    @Test
    @Order(6)
    void testIsValidDateValid() throws Exception{
        String validDate = LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        try (MockedStatic<CommonUtil> commonUtilMock = Mockito.mockStatic(CommonUtil.class)) {
            commonUtilMock.when(CommonUtil::getCurrentDateBasicISO).thenReturn(validDate);
            Method method = VaidateInputTrans.class.getDeclaredMethod("isValidDate", String.class);
            method.setAccessible(true);
            boolean res = (boolean) method.invoke(null,validDate);
            assertTrue(res);
        }
    }

    @Test
    @Order(7)
    void testIsValidDateInvalid() throws Exception {
        String invalidDate = "20250132"; // Invalid day
        Method method = VaidateInputTrans.class.getDeclaredMethod("isValidDate", String.class);
        method.setAccessible(true);
        boolean res = (boolean) method.invoke(null,invalidDate);
        assertFalse(res);
    }

    @Test
    @Order(8)
    void testIsValidAmountValid() throws Exception{
        Method method = VaidateInputTrans.class.getDeclaredMethod("isValidAmount", String.class);
        method.setAccessible(true);
        assertTrue((boolean) method.invoke(null,"100.00"));
        assertTrue((boolean) method.invoke(null,"0.01"));
    }

    @Test
    @Order(9)
    void testIsValidAmountInvalid() throws Exception{
        Method method = VaidateInputTrans.class.getDeclaredMethod("isValidAmount", String.class);
        method.setAccessible(true);
        assertFalse((boolean) method.invoke(null,"0.00"));
        assertFalse((boolean) method.invoke(null,"abc"));
        assertFalse((boolean) method.invoke(null,"100.001"));
    }

    @Test
    @Order(10)
    void testIsValidTxtTypeValid() throws Exception{
        Method method = VaidateInputTrans.class.getDeclaredMethod("isValidTxtType", String.class);
        method.setAccessible(true);
        assertTrue((boolean) method.invoke(null,"d"));
        assertTrue((boolean) method.invoke(null,"w"));
    }

    @Test
    @Order(11)
    void testIsValidTxtTypeInvalid() throws Exception{
        Method method = VaidateInputTrans.class.getDeclaredMethod("isValidTxtType", String.class);
        method.setAccessible(true);
        assertFalse((boolean) method.invoke(null,"x"));
        assertFalse((boolean) method.invoke(null,""));
    }

    @Test
    @Order(12)
    void testIsValidAccountValid() throws Exception{
        Method method = VaidateInputTrans.class.getDeclaredMethod("isValidAccount", String.class);
        method.setAccessible(true);
        assertTrue((boolean) method.invoke(null,"AB123"));
    }

    @Test
    @Order(13)
    void testIsValidAccountInvalid() throws Exception{
        Method method = VaidateInputTrans.class.getDeclaredMethod("isValidAccount", String.class);
        method.setAccessible(true);
        assertFalse((boolean) method.invoke(null,"12345"));
        assertFalse((boolean) method.invoke(null,"A1234"));
        assertFalse((boolean) method.invoke(null,"AB12"));
    }
    
}
