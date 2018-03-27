package com.n26.statistics.entities;

public class MomentSummary {
	private int quantity = 0;
	private double amount = 0.0;
	private double max = 0.0;
	private double min = Double.MAX_VALUE;
	
	public MomentSummary (double amount) {
		add(amount);
		quantity = 1;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void add(double 	pAmount){
		quantity = quantity+1;
		this.amount += pAmount;
		this.max = pAmount > this.max ? pAmount : this.max;
		this.min = pAmount <= this.min ? pAmount : this.min;
	}
	
	public double getMax(){
		return max;
	}
	
	public double getMin(){
		return min;
	}
	
}
