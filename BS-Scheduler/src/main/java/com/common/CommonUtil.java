/**
 * Projct: Banking System - EOM-Scheduler
 * This class is for common utility functions. 
 * Author: Arockiam Joseph
 * Created Date:10-01-2025
 * Version: 1.0
 */
package com.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class CommonUtil {
	
	/**
	 * 
	 * @return String date in YYYYMMdd format
	 */
	public static String getCurrentDateBasicISO() {
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
		String strDateBasicIso = formatter.format(date);
		
		return strDateBasicIso;
	}
	
	/**
	 * This method returns the first day of the current month in yyyyMMdd format
	 * @return
	 */
	public static String getFirstDayOfCurrMonthBasicISO() {
		LocalDate currentDate = LocalDate.now();
		
        // Get the first day of the current month
        LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
		String strDateBasicIso = formatter.format(firstDayOfMonth);
		return strDateBasicIso;
	}
	
	/**
	 * This method returns the last day of the current month in yyyyMMdd format
	 * @return
	 */
	public static String getLastDayOfCurrMonthBasicISO() {
		LocalDate currentDate = LocalDate.now();
		
        // Get the first day of the current month
        LocalDate lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
		String strDateBasicIso = formatter.format(lastDayOfMonth);
		return strDateBasicIso;
	}
	
	/**
	 * Description: This function removed all whitespaces including leading and trailing
	 * white spaces.
	 * @param inputStr
	 * @return return string.
	 */
	public static String removeAllWhiteSpaces(String inputStr) {
		 String result = inputStr.replaceAll("\\s+", "").trim();
		 return result;
	}
	/**
	 * Description: This method converts the double value to string
	 * @param value
	 * @return
	 */
	public static String convertNumberToString(double value) {
		return String.valueOf(value);
	}
	/**
	 * Description: This method converts the int value to string
	 * @param value
	 * @return
	 */
	public static String convertNumberToString(int value) {
		return String.valueOf(value);
	}
	
	/**
	 * Description: This method calculates interest for the given rate and amount
	 * @param amount
	 * @param intRate
	 * @return
	 */
	public static double calculateInterest(double amount, double intRate) {
		double earnedInterest = ((amount*(intRate/100))/365);
		return earnedInterest;
	}
	
	/**
	 * 
	 * @param value
	 * @param decimalValue
	 * @return
	 */
	public static double roundValue(double value, int decimalValue) {
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(decimalValue, RoundingMode.HALF_UP); 
		return bd.doubleValue();
	}
}
