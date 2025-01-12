package com.common;

import static org.junit.jupiter.api.Assertions.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommonUtilTest {

	@Test
	void testGetCurrentDateBasicISO() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
        String expectedDate = formatter.format(today);
        String actualDate = CommonUtil.getCurrentDateBasicISO();
        assertEquals(expectedDate, actualDate);
	}

	
	@Test
	void testRemoveAllWhiteSpaces() {
        String input = "  Hi   AwesomeGIC Bank  ";
        String expectedOutput = "HiAwesomeGICBank";
        String actualOutput = CommonUtil.removeAllWhiteSpaces(input);
        assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	void testConvertNumberToStringDouble() {
        double input = 123.456;
        String expectedOutput = "123.456";
        String actualOutput = CommonUtil.convertNumberToString(input);
        assertEquals(expectedOutput, actualOutput);
	}

	@Test
	void testConvertNumberToStringInt() {
        int input = 123;
        String expectedOutput = "123";
        String actualOutput = CommonUtil.convertNumberToString(input);
        assertEquals(expectedOutput, actualOutput);
	}

	@Test
	void testCalculateInterest() {
		double amount = 1000.0;
		double rate = 5.0;
		double expectedInterest = 0.137;
		DecimalFormat decimalFormat = new DecimalFormat("#.###");
		double actualInterest = CommonUtil.calculateInterest(amount, rate);
		double earnInterest = Double.valueOf(decimalFormat.format(actualInterest));
		assertEquals(expectedInterest, earnInterest);
	}
	
	@Test
    void testRoundValueWithPositiveValue() {
        double value = 123.456789;
        int decimalPlaces = 2;
        double expected = 123.46; // Rounded to 2 decimal places

        double actual = CommonUtil.roundValue(value, decimalPlaces);

        assertEquals(expected, actual);
    }

    @Test
    void testRoundValueWithNegativeValue() {
        double value = -123.456789;
        int decimalPlaces = 3;
        double expected = -123.457; // Rounded to 3 decimal places

        double actual = CommonUtil.roundValue(value, decimalPlaces);

        assertEquals(expected, actual);
    }
}
