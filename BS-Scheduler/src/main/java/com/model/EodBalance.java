package com.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eod_balance")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EodBalance {
	
	@EmbeddedId
	private EodId id;
	
	@Column(name = "balance")
	private double balance;
	
	@Column(name = "app_int_rule")
	private String appIntRule;
	
	@Column(name = "app_int_rate")
	private double appIntRate;
	
	@Column(name = "earned_interest")
	private double earnedInterest;
	
}
