/**
 * Projct: Banking System - EOM-Scheduler
 * This is the service class performs the eom calculation and updates the db tables
 * Author: Arockiam Joseph
 * Created Date:10-01-2025
 * Version: 1.0
 */
package com.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.CommonUtil;
import com.common.Constants;
import com.model.Account;
import com.model.AcctTransaction;
import com.model.EodBalance;
import com.model.EodId;
import com.repository.AccTransactionRepository;
import com.repository.AccountRepository;
import com.repository.EodBalanceRepository;

@Service
public class EomInterestService {

	private static final Logger logger = LogManager.getLogger(EomInterestService.class);

	@Autowired
	private EodBalanceRepository eodRepo;
	
	@Autowired
	private AccountRepository acctRepo;

	@Autowired
	private AccTransactionRepository accTransRepo;
	
	/**
	 * This method is to calculate the interest and update the 
	 * balance in account table, add entry with I in account transaction table and
	 * finally update the eod balance for that day(last day of the month).
	 * @return
	 */
	public boolean calculateEomInterest() {
		try {
			logger.info("----started calculateEomInterest------");
			String firstDayOfCurMonth = CommonUtil.getFirstDayOfCurrMonthBasicISO();
			String lastDayOfCurMonth = CommonUtil.getLastDayOfCurrMonthBasicISO();
			String currDate = CommonUtil.getCurrentDateBasicISO();
			List<Object[]> interestList = eodRepo.computeInterest(firstDayOfCurMonth, lastDayOfCurMonth);
			List<Account> accList = new ArrayList<Account>();
			List<EodBalance> eodList = new ArrayList<EodBalance>();
			// update account table
			// account account transaction table.
			for(Object[] o: interestList) {
				String accountNo = o[0].toString();
				double earnInterest = Double.valueOf(o[1].toString());
				Optional<Account> accOpt = acctRepo.findById(accountNo);
				EodId eodid = EodId.builder().accountNo(accountNo)
						      .eodDate(currDate)
						      .build();
				Optional<EodBalance> eodOpt = eodRepo.findById(eodid);
				if(accOpt.isPresent()) {
					Account account = accOpt.get();
					double newAccBal = CommonUtil.roundValue((account.getBalance()+earnInterest), 2);
					account.setBalance(newAccBal);
					
					//set acct transaction.
					AcctTransaction accTrans = AcctTransaction.builder()
							  .amount(earnInterest)
							  .txnType(Constants.TXN_TYPE.I.name())
							  .txnDate(currDate)
							  .txnId("")
							  .account(account)
							  .balance(newAccBal)
							  .build();
					account.setTransactions(List.of(accTrans));
					accList.add(account);
				}
				if(eodOpt.isPresent()) {
					EodBalance eodBalance = eodOpt.get();
					double newEodBalace = CommonUtil.roundValue((eodBalance.getBalance()+earnInterest), 2);
					eodBalance.setBalance(newEodBalace);
					eodList.add(eodBalance);
				}
			}
			if(accList.size()>0)
				acctRepo.saveAll(accList);
			if(eodList.size()>0)
				eodRepo.saveAll(eodList);
			logger.info("----ended calculateEomInterest----");
			return true;
		}catch (Exception e) {
			logger.error("Error in calculateEomInterest:"+e.getMessage());
			return false;
		}
	}
	/**
	 * 
	 * @param dateStr
	 * @param txnType
	 * @return
	 */
	public boolean isEomProcessed(String dateStr, String txnType) {
		int accTrans = accTransRepo.findTopByDateAndTxnType(dateStr, txnType);
		System.out.println("accTrans:"+accTrans);
		if (accTrans!=0) {
			return true;
		}else {
			return false;
		}
	}
}
