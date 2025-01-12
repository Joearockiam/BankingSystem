package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.model.AccountTransaction;
import com.model.Account;


@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Integer>{

	List<AccountTransaction> findByAccount_AccountNoAndTxnDateAndTxnDateIsNotNullOrderByUpdatedDateDesc(String accountNo, String txnDate);

	@Query("SELECT acTrans FROM AccountTransaction acTrans WHERE acTrans.account.accountNo=:accNo and substring(acTrans.txnDate,0,7)=:monthYear order by acTrans.updatedDate asc")
	List<AccountTransaction> findByAccountNoYearMonth(@Param("accNo") String accNo, @Param("monthYear") String monthYear);
}
