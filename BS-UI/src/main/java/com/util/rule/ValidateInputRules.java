/**
 * Projct: Banking System - UI
 * This contains the methods to validate the rules input data. 
 * Author: Arockiam Joseph
 * Created Date:12-01-2025
 * Version: 1.0
 */
package com.util.rule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.common.CommonUtil;

public class ValidateInputRules {
	private static final Logger logger = LogManager.getLogger(ValidateInputRules.class);

		
	/**
	 * This method validate the input data
	 * @param dataStr
	 * @return
	 */
	public static boolean isValid(String[] dataStr) {
		
		String dateStr = dataStr[0];
		String ruleStr = dataStr[1];
		String rateStr = dataStr[2];
		
		if (Objects.isNull(dateStr) || Objects.isNull(ruleStr) || Objects.isNull(rateStr))
			return false;
		
		if(isValidDate(dateStr) && isValidRule(ruleStr) &&  isValidRate(rateStr)) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * This method validate the entered date is valid or not
	 * @param dateStr
	 * @return
	 */
	private static boolean isValidDate(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		try {
			LocalDate.parse(dateStr,formatter);
			if (dateStr.equals(CommonUtil.getCurrentDateBasicISO()))
				return true;
			else
				return false;
		}catch (DateTimeParseException e) {
			logger.error("The date format is incorrect.:"+dateStr);
			return false;
		}
	}
	
	/**
	 * This method validate the entered rule string is valid or not
	 * @param ruleStr
	 * @return
	 */
	private static boolean isValidRule(String ruleStr) {
		if (ruleStr.length()<255)
			return true;
		else
			return false;
	}
	
	/**
	 * This method validate the entered rate is valid or not
	 * @param rateStr
	 * @return
	 */
	private static boolean isValidRate(String rateStr) {
		String regex = "(\\d+)(\\.\\d{1,2})?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(rateStr);
		if (matcher.matches()) {
			Double value = Double.parseDouble(rateStr);
			if (value==0.0) {
				logger.info("The entered rate is not valid.");
				return false;
				}
			else
				return true;
        } else {
            return false;
        }
	}
}
