/**
 * Projct: Banking System - EOM-Scheduler
 * This class contains the business logic for eod scheduler. 
 * Author: Arockiam Joseph
 * Created Date:11-01-2025
 * Version: 1.0
 */
package com.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.CommonUtil;
import com.model.EodBalance;
import com.model.EodId;
import com.model.Rule;
import com.repository.AccTransactionRepository;
import com.repository.EodBalanceRepository;
import com.repository.RuleRepository;
import com.scheduler.Scheduler;

@Service
public class EodBalanceService {
	private static final Logger logger = LogManager.getLogger(EodBalanceService.class);

	@Autowired
	private AccTransactionRepository accTransRepo;
	
	@Autowired
	private EodBalanceRepository eodRepo;
	
	@Autowired
	private RuleRepository ruleRepo;
	
	/**
	 * This method retrives all the transactions.
	 * @param accountNo
	 * @param currDate
	 * @return
	 */
	private List<EodBalance> retrieveAllTranaction(){
		List<EodBalance> retList = new ArrayList<EodBalance>();
		List<Object[]> list = this.accTransRepo.computeEodForAllAccount();
		EodBalance eod = null;
		EodId eodId = null;
		String currentDateStr = CommonUtil.getCurrentDateBasicISO();
		String[] ruleArr = getLatestRule();
		for (Object[] o : list) {
			eod = new EodBalance();
			eodId = new EodId();
			eodId.setEodDate(currentDateStr);
			eodId.setAccountNo(o[0].toString());
			eod.setId(eodId);
			eod.setBalance(Double.valueOf(o[1].toString()));
			eod.setAppIntRule(ruleArr[1]);
			eod.setAppIntRate(Double.valueOf(ruleArr[2]));
			eod.setEarnedInterest(CommonUtil.calculateInterest(eod.getBalance(), eod.getAppIntRate()));
			retList.add(eod);
		}
		return retList;
	}
	
	/**
	 * This method process the eod balance for each account on each day
	 * @return true or false.
	 */
	public boolean processEodBalance() {
		try {
			List<EodBalance> aList = retrieveAllTranaction();
			if (aList.size() > 0) {
				eodRepo.saveAll(aList);
				logger.info("The eod processing complted successfully for "+aList.size()+" accounts");
			}
			
			return true;
		} catch (Exception e) {
			logger.error("The eod processing is unsuccesdful"+e.getMessage());
			return false;
		}
	}
	
	/**
	 * This method is to check whether the eod balance is computed for the given date. 
	 * @param currentDateStr
	 * @return true or false
	 */
	public boolean isEodProcessed(String currentDateStr) {
		int count =  this.eodRepo.countDistinctEodDate(currentDateStr);
		if(count==0) {
			logger.info("The isEodProcessed() count:"+count);
			return false;
		}else {
			logger.info("The isEodProcessed() count:"+count);
			return true;
		}
	}
	/**
	 * This method is to get the latest rule for the interest
	 * @return String[]
	 */
	private String[] getLatestRule() {
		List<Object[]> ruleList= this.ruleRepo.getLatestRule();
		String[] strRule = new String[3];
		for(Object[] o: ruleList) {
			strRule[0]= o[0].toString();
			strRule[1]= o[1].toString();
			strRule[2]= o[2].toString();
		}
		logger.info(" The latest rule is "+strRule[0]+","+strRule[1]+","+strRule[2]);
		return strRule;
	}
}
