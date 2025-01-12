/**
 * Projct: Banking System - UI
 * This is class for handling the printing statements. 
 * Author: Arockiam Joseph
 * Created Date:12-01-2025
 * Version: 1.0
 */
package com.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Account;
import com.model.AccountTransaction;
import com.repository.AccountRepository;
import com.repository.AccountTransactionRepository;

@Service
public class PrintStatementService {
	private static final Logger logger = LogManager.getLogger(PrintStatementService.class);

	@Autowired
	private AccountTransactionRepository accTransRepository;
	
	@Autowired
	private AccountRepository accRepo;
	
	/**
	 * This method is to get all the transactions for the given account, month and year.
	 * @param accountNo
	 * @param txnDateStr
	 * @return
	 */
	public List<AccountTransaction> retrieveAllTransaction(String accountNo, String monthYear){
		return accTransRepository.findByAccountNoYearMonth(accountNo,monthYear);
	}
	
	/**
	 * This function is to check whether the account number entered is exists or not.
	 * @param accNo
	 * @return
	 */
	public boolean isAccountExist(String accNo) {
		Optional<Account> optAccount = this.accRepo.findById(accNo);
		if (optAccount.isPresent()) {
			logger.info("Account exist in db.");
			return true;
		}else {
			logger.info("Account not exist in db.");
			return false;
		}
	}
}
