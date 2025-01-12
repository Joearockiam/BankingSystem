/**
 * Projct: Banking System - UI
 * This is class to handle the rule creation and updation. 
 * Author: Arockiam Joseph
 * Created Date:12-01-2025
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

import com.model.Rule;
import com.repository.RuleRepository;

@Service
public class RulesService {
	
	private static final Logger logger = LogManager.getLogger(RulesService.class);

	@Autowired
	private RuleRepository ruleRepository;
	
	/**
	 * This method will create rule in the db and update if already exists.
	 * @param dateStr
	 * @param ruleId
	 * @param rate
	 * @return
	 */
	public boolean createUpdateRule(String dateStr, String ruleId, double rate) {
		try {
			Optional<Rule> ruleObj = getRule(dateStr);
			Rule rule = null;
			if (ruleObj.isEmpty()) {
				rule = Rule.builder().ruleDate(dateStr).ruleId(ruleId).rate(rate).build();
			} else {
				rule = ruleObj.get();
				rule.setRate(rate);
				rule.setRuleId(ruleId);
			}
			ruleRepository.save(rule);
			logger.info("The rule save/update is successful");
			return true;
		} catch (Exception e) {
			logger.error("The rule save/update is unsuccessful:"+e.getMessage());
			return false;
		}
	}
	/**
	 * This method retrive all rules form the table.
	 * @return
	 */
	public List<Rule> retriveAllRules(){
		return ruleRepository.findAll();
	}
	/**
	 * This method will retrieve rule for the given date.
	 * @param dateStrId
	 * @return
	 */
	private Optional<Rule> getRule(String dateStrId) {
		return this.ruleRepository.findById(dateStrId);
	}
}
