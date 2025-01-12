package com.common;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.context.annotation.Description;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommonUtilTest {

	@Test
	@Order(1)
	@Description("Test the getCurrentDateBasicISO() for success case.")
    public void testGetCurrentDateBasicISO() {
        // prepare
		LocalDate expectedDate = LocalDate.now();
        String expectedDateString = DateTimeFormatter.BASIC_ISO_DATE.format(expectedDate);

        // Act
        String actualDateString = CommonUtil.getCurrentDateBasicISO();

        // Assert
        assertEquals(expectedDateString, actualDateString);
    }
	@Test
	@Order(2)
	@Description("Test the getCurrentDateBasicISO() for failure case.")
    public void testGetCurrentDateBasicISOFailure() {
        // prepare
		String dateStr = "20250110";
        // Act
        String actualDateString = CommonUtil.getCurrentDateBasicISO();

        // Assert
        assertNotEquals(dateStr, actualDateString);
    }
	
    @Test
    @Order(3)
	@Description("Test the RemoveAllWhiteSpaces for success case.")
    public void testRemoveAllWhiteSpaces() {
        // prepare
        String input = "   This  is   a test    string  ";
        String expected = "Thisisateststring";

        String actual = CommonUtil.removeAllWhiteSpaces(input);

        assertEquals(expected, actual);
    }
    
    @Test
    @Order(4)
	@Description("Test the ConvertDoubleToString for success case.")
    public void testConvertDoubleToString() {
        // prepare
        double value = 12345.678;
        String expected = "12345.678";

        // Act
        String actual = CommonUtil.convertNumberToString(value);

        // Assert
        assertEquals(expected, actual);
    }
    
	
    @Test
    @Order(5)
	@Description("Test the ConvertIntToString for success case.")
    public void testConvertIntToString() {
        // prepare
        int value = 12345;
        String expected = "12345";

        // Act
        String actual = CommonUtil.convertNumberToString(value);

        // Assert
        assertEquals(expected, actual);
    }
    

    @Test
    @Order(6)
	@Description("Test the ParseUserInput for success case.")
    public void testParseUserInput() {
        // prepare
        String[] input = {"  test1  ", " test2", "", "  ","   test3"};
        String[] expected = {"test1", "test2", "test3"};

        // Act
        String[] actual = CommonUtil.parseUserInput(input);

        // Assert
        assertArrayEquals(expected, actual, "The parsed input should remove spaces and empty elements.");
    }

}
