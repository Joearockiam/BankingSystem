/**
 * Projct: Banking System - UI
 * This is class for handling the input transactions such as Deposit and withdrawal. 
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

import com.util.input.InputTransUtil;
import com.model.Account;
import com.model.AccountTransaction;
import com.repository.AccountRepository;
import com.repository.AccountTransactionRepository;

@Service
public class InputTransactionService {
	private static final Logger logger = LogManager.getLogger(InputTransactionService.class);

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountTransactionRepository accountTransService;
	
	/**
	 * This method checks the transaction is 'W' and not first transaction.
	 * @param accountNo
	 * @param txnType
	 * @return
	 */
	public boolean isValidTransaction(String accountNo, String txnType) {
		logger.info("...The isValidTransaction() is executed...");
		Optional<Account> optAcc = getAccount(accountNo);
		if (optAcc.isEmpty() && txnType.equalsIgnoreCase("w"))
			return false;
		else
			return true;
	}
	/**
	 * This method process the given transaction.
	 * @param dateStr
	 * @param accountNo
	 * @param txnType
	 * @param amount
	 * @return
	 */
	public boolean process(String dateStr, String accountNo, String txnType, double amount) {
		
		/*
		 * 1. update balance in account table if account number 
		 *    already exists other wise, create new record in account table
		 * 2. add one record in account transaction table with amount and 
		 *    update the balance with the current deposit if the account transaction
		 *    is happened already. Otherwise, just update the balance with the 
		 *    same deposit amount. 
		 */
		try {
			Optional<Account> opAcc = getAccount(accountNo);

			if (opAcc.isPresent()) {
				Account account = opAcc.get();
				if(txnType.equalsIgnoreCase("d")) {
					AccountTransaction accTrans = AccountTransaction.builder()
							  .amount(amount)
							  .txnType(txnType)
							  .txnDate(dateStr)
							  .txnId(getTxnId(accountNo, dateStr))
							  .account(account)
							  .balance(account.getBalance()+amount)
							  .build();
					account.setBalance(account.getBalance()+amount);
					account.setTransactions(List.of(accTrans));
				}else {
					AccountTransaction accTrans = AccountTransaction.builder()
							  .amount(amount)
							  .txnType(txnType)
							  .txnDate(dateStr)
							  .txnId(getTxnId(accountNo, dateStr))
							  .account(account)
							  .balance(account.getBalance()-amount)
							  .build();
					account.setBalance(account.getBalance()- amount);
					account.setTransactions(List.of(accTrans));
				}
				updateAccount(account);
				
			}else {
				Account account = Account.builder()
								  .accountNo(accountNo)
								  .balance(amount)
								  .build();
				AccountTransaction accTrans = AccountTransaction.builder()
						  .amount(amount)
						  .txnType(txnType)
						  .txnDate(dateStr)
						  .txnId(getTxnId(accountNo, dateStr))
						  .account(account)
						  .build();
				accTrans.setBalance(amount);
				account.setTransactions(List.of(accTrans));
				this.accountRepository.save(account);
			}
			// update account transaction
			return true;
		} catch (Exception e) {
			logger.info("Error in processing the input transaction:"+e.getMessage());
			return false;
		}
		
	}
	/**
	 * This method is to update the account table	
	 * @param account
	 */
	private void updateAccount(Account account) {
		this.accountRepository.save(account);
		logger.info("account transaction saved successfully");
	}
	
	/**
	 * 
	 * @param accountNo
	 * @return
	 */
	private Optional<Account> getAccount(String accountNo) {
		Optional<Account> optionAcc = this.accountRepository.findById(accountNo);
		return optionAcc;
	}
	
	/**
	 * This method is to get the balance of an account no.
	 * @param accountNo
	 * @return
	 */
	public double getBalance(String accountNo) {
		double balance = 0;
		Optional<Account> accOpt = getAccount(accountNo);
		if (accOpt.isEmpty())
			balance = 0;
		else
			balance = accOpt.get().getBalance();
		return balance;
	}
	
	/**
	 * This method is to get the next transaction id
	 * @param accountNo
	 * @param dateStr
	 * @return
	 */
	private String getTxnId(String accountNo, String dateStr) {
		List<AccountTransaction> accTransList = this.accountTransService.findByAccount_AccountNoAndTxnDateAndTxnDateIsNotNullOrderByUpdatedDateDesc(accountNo, dateStr);
		if(accTransList.size()==0) {
			return dateStr+"-"+"01";
		}else {
			return InputTransUtil.getNextTxnId(accTransList.get(0).getTxnId());
		}
	}
	
	/**
	 * This methos is to get the number of Transaction for the given account and date.
	 * @param accountNo
	 * @param dateStr
	 * @return
	 */
	public int getNumberofTransaction(String accountNo, String dateStr) {
		List<AccountTransaction> accTransList = this.accountTransService.findByAccount_AccountNoAndTxnDateAndTxnDateIsNotNullOrderByUpdatedDateDesc(accountNo, dateStr);
		return accTransList.size();
	}
	
	/**
	 * This method is to retrieve all the transactions.
	 * @param accountNo
	 * @param dateStr
	 * @return
	 */
	public List<AccountTransaction> retrieveAllTransaction(String accountNo, String dateStr){
		return this.accountTransService.findByAccount_AccountNoAndTxnDateAndTxnDateIsNotNullOrderByUpdatedDateDesc(accountNo, dateStr);
	}
}
