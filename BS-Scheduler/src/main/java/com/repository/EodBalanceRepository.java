/**
 * Projct: Banking System - EOM-Scheduler
 * This interface is for eod_balance table. 
 * Author: Arockiam Joseph
 * Created Date:10-01-2025
 * Version: 1.0
 */
package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.model.EodBalance;
import com.model.EodId;

@Repository
public interface EodBalanceRepository extends JpaRepository<EodBalance, EodId>{

	@Query(value = "select count(eod_date) from eod_balance where eod_date=:curDateStr", nativeQuery = true)
	int countDistinctEodDate(@Param("curDateStr") String curDateStr);
	
	@Query(value = "select account_no, round(sum(earned_interest),2) "
			+ " from eod_balance "
			+ "where to_date(eod_date,'yyyyMMdd') between to_date(:startDate,'yyyyMMdd') and to_date(:endDate,'yyyyMMdd') "
			+ "group by account_no;", nativeQuery = true)
	List<Object[]> computeInterest(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
