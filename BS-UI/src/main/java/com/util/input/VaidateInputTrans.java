/**
 * Projct: Banking System - UI
 * This contains the methods to validate the input related to Transaction entered by the user. 
 * Author: Arockiam Joseph
 * Created Date:12-01-2025
 * Version: 1.0
 */
package com.util.input;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.common.CommonUtil;

public class VaidateInputTrans {
	private static final Logger logger = LogManager.getLogger(VaidateInputTrans.class);
	
	/**
	 * This method validate the each input data
	 * @param str
	 * @return
	 */
	public static boolean isValid(String[] str) {
		String dateStr = str[0];
		String accountStr = str[1];
		String typeStr = str[2];
		String amountStr = str[3];
		
		if (Objects.isNull(dateStr) || Objects.isNull(accountStr) || Objects.isNull(typeStr) || Objects.isNull(amountStr))
			return false;
		
		if (isValidDate(dateStr) && isValidAmount(amountStr) && isValidTxtType(typeStr) && isValidAccount(accountStr)){
			return true;
		}else {
			return false;
		}
		
	}
	/**
	 * This method is to validate the date.
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
			logger.error("error in parsing date isValidDate:"+e.getMessage());
			return false;
		}
	}
	
	/**
	 * This method validate the entered amount is valid or not.
	 * @param amountStr
	 * @return
	 */
	private static boolean isValidAmount(String amountStr) {
		String regex = "(\\d+)(\\.\\d{1,2})?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(amountStr);
		if (matcher.matches()) {
			Double value = Double.parseDouble(amountStr);
			if (value==0.0)
				return false;
			else
				return true;
        } else {
            return false;
        }
	}
	/**
	 * This method validate the entered transaction type is valid or not.
	 * @param txtTypeStr
	 * @return
	 */
	private static boolean isValidTxtType(String txtTypeStr) {
		if (txtTypeStr.equalsIgnoreCase("d") || txtTypeStr.equalsIgnoreCase("w")) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * This method validate the entered account is valid or not.
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
