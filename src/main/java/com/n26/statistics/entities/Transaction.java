package com.n26.statistics.entities;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping 
public class Transaction {
	private double amount;
	private long timestamp;
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
