/**
 * Projct: Banking System - EOM-Scheduler
 * This interface is for Account Transaction table. 
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

import com.model.AcctTransaction;

@Repository
public interface AccTransactionRepository extends JpaRepository<AcctTransaction, Integer>{

	@Query(value="select distinct on (account_no) account_no, balance from account_txn order by account_no,updated_date desc",nativeQuery = true)
	List<Object[]> computeEodForAllAccount();
	
	@Query(value = "Select count(distinct txn_date) from account_txn txn where txn.txn_date=:dateStr and txn.txn_type=:txnType", nativeQuery = true)
	int findTopByDateAndTxnType(@Param("dateStr") String dateStr, @Param("txnType") String txnType);
}
