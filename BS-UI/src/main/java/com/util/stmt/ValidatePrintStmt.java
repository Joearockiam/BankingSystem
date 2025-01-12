/**
 * Projct: Banking System - UI
 * This contains the methods to validate the Print statement input data. 
 * Author: Arockiam Joseph
 * Created Date:12-01-2025
 * Version: 1.0
 */
package com.util.stmt;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ValidatePrintStmt {
	
	private static final Logger logger = LogManager.getLogger(ValidatePrintStmt.class);

	
	/**
	 * This method is to check whether the input data is valid or not.
	 * @param str
	 * @return
	 */
	public static boolean isValid(String[] str) {
		String accountStr = str[0];
		String monthYr = str[1];
		
		if (Objects.isNull(monthYr) || Objects.isNull(accountStr))
			return false;
		
		if (isValidYearMonth(monthYr) && isValidAccount(accountStr)){
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * This method is to check whether the year month is valid format
	 * @param yearMonthStr
	 * @return
	 */
	private static boolean isValidYearMonth(String yearMonthStr) {
		String regex = "^(19|20)\\d{2}(0[1-9]|1[0-2])$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(yearMonthStr);
		if (matcher.matches()) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * This method is to check whether the account number entered is in valid format
	 * @param accountStr
	 * @return
	 */
	private static boolean isValidAccount(String accountStr) {
		String regex ="^[A-Za-z]{2}\\d{3}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(accountStr);
		if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
	}
}
