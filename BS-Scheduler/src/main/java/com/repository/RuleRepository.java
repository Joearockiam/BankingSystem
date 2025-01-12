/**
 * Projct: Banking System - EOM-Scheduler
 * This interface is for rule table. 
 * Author: Arockiam Joseph
 * Created Date:10-01-2025
 * Version: 1.0
 */
package com.repository;

import java.util.List;

import org.hibernate.type.TrueFalseConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.model.Rule;

@Repository
public interface RuleRepository extends JpaRepository<Rule, String>{

	@Query(value = " select *, to_date(rule_date,'yyyyMMdd') as cDate from rules order by \r\n"
			+ "  cDate desc limit 1", nativeQuery = true)
	List<Object[]> getLatestRule();
}
