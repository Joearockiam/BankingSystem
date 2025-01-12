package com.model;

import java.sql.Timestamp;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_txn")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcctTransaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "txn_id")
	private String txnId;
	
	@Column(name = "txn_date")
	private String txnDate;
	
	@Column(name = "txn_type")
	private String txnType;
	
	@Column(name = "amount")
	private double amount;
	
	@Column(name = "balance")
	private double balance;
	
	@Column(name = "updated_date")
	@UpdateTimestamp
	private Timestamp updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_no")
	private Account account;
}
