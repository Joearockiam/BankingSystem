/**
 * Projct: Banking System - UI
 * This is Common utility class used in the project. 
 * Author: Arockiam Joseph
 * Created Date:12-01-2025
 * Version: 1.0
 */
package com.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
	 * This method is to convert the double to string
	 * @param value
	 * @return
	 */
	public static String convertNumberToString(double value) {
		return String.valueOf(value);
	}
	/**
	 * This method is to convert integer to string
	 * @param value
	 * @return
	 */
	public static String convertNumberToString(int value) {
		return String.valueOf(value);
	}
	/**
	 * This method is to clear the command line window.
	 */
	public static void clear() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                // Windows
                Runtime.getRuntime().exec("cls");
            } else {
                // Linux or macOS
                Runtime.getRuntime().exec("clear");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * This method is to parse the userinput, ie. removes the space
	 * @param userInput
	 * @return
	 */
	public static String[] parseUserInput(String[] userInput) {
		StringBuilder sb = new StringBuilder();
		
		for(String s: userInput) {
			String strim = s.trim();
			if(strim.length()!=0) {
				sb.append(strim);
				sb.append(",");
			}
		}
		String[] retArray = sb.substring(0, (sb.length()-1)).toString().split(",");
		return retArray;
	}
}
