package com.util.rule;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.util.input.VaidateInputTrans;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ValidateInputRulesTest {

    @Test
    @Order(1)
    void testValidInput() {
        String[] validData = {"20250112", "Valid Rule", "10.50"};
        boolean result = ValidateInputRules.isValid(validData);
        assertTrue(result);
    }

    @Test
    @Order(2)
    void testInvalidDate() {
        String[] invalidDateData = {"20250112222", "Valid Rule", "10.50"};
        boolean result = ValidateInputRules.isValid(invalidDateData);
        assertFalse(result);
    }

    @Test
    @Order(3)
    void testInvalidRule() {
        String[] invalidRuleData = {"20250112", "This rule string is way too long and exceeds the maximum length of 255 characters, making it invalid for testing purposes.This rule string is way too long and exceeds the maximum length of 255 characters, making it invalid for testing purposes. This rule string is way too long and exceeds the maximum length of 255 characters, making it invalid for testing purposes.", "10.50"};
        boolean result = ValidateInputRules.isValid(invalidRuleData);
        assertFalse(result);
    }

    @Test
    @Order(4)
    void testInvalidRate() {
        String[] invalidRateData = {"20250112", "Valid Rule", "0.00"};
        boolean result = ValidateInputRules.isValid(invalidRateData);
        assertFalse(result);
    }

    @Test
    @Order(5)
    void testNullDate() {
        String[] nullDateData = {null, "Valid Rule", "10.50"};
        boolean result = ValidateInputRules.isValid(nullDateData);
        assertFalse(result);
    }

    @Test
    @Order(6)
    void testNullRule() {
        String[] nullRuleData = {"20250112", null, "10.50"};
        boolean result = ValidateInputRules.isValid(nullRuleData);
        assertFalse(result);
    }

    @Test
    @Order(7)
    void testNullRate() {
        String[] nullRateData = {"20250112", "Valid Rule", null};
        boolean result = ValidateInputRules.isValid(nullRateData);
        assertFalse(result);
    }

    @Test
    @Order(8)
    void testInvalidRateFormat() {
        String[] invalidRateFormatData = {"20250112", "Valid Rule", "10,50"};
        boolean result = ValidateInputRules.isValid(invalidRateFormatData);
        assertFalse(result, "The input data should be invalid due to incorrect rate format.");
    }
    @Test
    @Order(9)
    void testValidRate() throws Exception{
        String validRate = "10.50";
        Method method = ValidateInputRules.class.getDeclaredMethod("isValidRate", String.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null,validRate);
        assertTrue(result);
    }


    @Test
    @Order(10)
    void testValidRateWithoutDecimals() throws Exception{
        String validRate = "10";
        Method method = ValidateInputRules.class.getDeclaredMethod("isValidRate", String.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null,validRate);
        assertTrue(result);
    }

    @Test
    @Order(11)
    void testInvalidRateWithMoreThanTwoDecimalPlaces() throws Exception{
        String invalidRate = "10.123";
        Method method = ValidateInputRules.class.getDeclaredMethod("isValidRate", String.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null,invalidRate);
        assertFalse(result);
    }

    @Test
    @Order(12)
    void testInvalidRateWithNonNumericValue() throws Exception{
        String invalidRate = "abc";
        Method method = ValidateInputRules.class.getDeclaredMethod("isValidRate", String.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null,invalidRate);
        assertFalse(result);
    }

    @Test
    @Order(13)
    void testInvalidRateWithNegativeValue() throws Exception{
        String invalidRate = "-10.50";
        Method method = ValidateInputRules.class.getDeclaredMethod("isValidRate", String.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null,invalidRate);
        assertFalse(result);
    }

    @Test
    @Order(14)
    void testInvalidRateWithComma() throws Exception{
        String invalidRate = "10,50";
        Method method = ValidateInputRules.class.getDeclaredMethod("isValidRate", String.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null,invalidRate);
        assertFalse(result);
    }
    
}
