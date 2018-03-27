package com.n26.statistics.entities;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public class Statistics {
	private double sum;
	//private double avg;
	private double max;
	private double min;
	private long count;
	
	
	public Statistics(double sum, double max, double min, long count) {
		this.sum = sum;
		this.max = max;
		this.min = min;
		this.count = count;
	}
	
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	public double getAvg() {
		return count > 0? sum/count: 0.0;
	}

	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}

	public void clear() {
		this.max = 0.0;
		this.min = 0.0;
		this.count = 0;
		this.sum = 0.0;
	}
	
	
	
}
