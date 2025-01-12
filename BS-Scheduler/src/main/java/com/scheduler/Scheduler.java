/**
 * Projct: Banking System - Scheduler
 * This is the scheduler that executes a EOD and EMO task periodically. 
 * Author: Arockiam Joseph
 * Created Date:12-01-2025
 * Version: 1.0
 */
package com.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.common.CommonUtil;
import com.service.EodBalanceService;
import com.service.EomInterestService;


@Component
public class Scheduler {
	private static final Logger logger = LogManager.getLogger(Scheduler.class);

	@Autowired
	private EodBalanceService eodService;
	
	@Autowired
	private EomInterestService eomService;
	
	@Scheduled(cron = "#{@eodCronExpression}")
	public void runEodTask() {
		logger.info("Eod Task executed at : " + LocalDateTime.now());
		if(eodService.isEodProcessed(CommonUtil.getCurrentDateBasicISO())) {
			System.out.println("EOD already computed");
			logger.info("EOD already computed");
		}else {
			System.out.println("EOD Not computed");
			logger.info("EOD Not computed");
			if(eodService.processEodBalance())
				logger.info("EOD balance processed successfully");
			else
				logger.info("EOD balance process unsuccessful");
		}
	}
	
	@Scheduled(cron = "#{@eomCronExpression}")
	public void runEomTask() {
		if (!isLastDayOfMonth())
			return;
		logger.info("Task (EomTask) execution started at : " + LocalDateTime.now());
		boolean eomProcessStatus = eomService.isEomProcessed(CommonUtil.getCurrentDateBasicISO(), "I");
		if (eomProcessStatus) {
			System.out.println("EOM already processed...");
			logger.info("EOM already processed for this month...");
		} else {
			if (eomService.calculateEomInterest()) {
				System.out.println("EOM interest processed successfully");
				logger.info("EOM interest processed successfully");
			} else {
				System.out.println("EOM interest process unsuccessful");
				logger.info("EOM interest process unsuccessful");
			}
			logger.info("Task (EomScheduler) execution ended at : " + LocalDateTime.now());
		}
	}
	/**
	 * This method is to check whether today is the last from 28th onwards.
	 * @return
	 */
	private boolean isLastDayOfMonth() {
        /**
         * check if tomorrow is the first day of the next month
         */
		LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        return tomorrow.getDayOfMonth() == 1;
    }
}
