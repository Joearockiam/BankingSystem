package com.model;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rule {
	
	@Id
	@Column(name = "rule_date")
	private String ruleDate;
	
	@Column(name = "rule_id")
	private String ruleId;
	
	@Column(name = "rate")
	private double rate;
	
}
