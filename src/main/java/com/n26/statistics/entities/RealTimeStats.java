package com.n26.statistics.entities;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class RealTimeStats {
	ConcurrentHashMap <Long, MomentSummary> transactions = new ConcurrentHashMap<Long, MomentSummary>();
	Statistics stats = new Statistics(0.0,0.0,0.0,0);
	
	private long lastUpdate = 0;
	
	public void update(Transaction transaction) {
		long timeStamp = transaction.getTimestamp();
		double differenceAmount = transaction.getAmount();
		long differenceQty = 1;
		if (lastUpdate != timeStamp){
			synchronized (transactions){
					if (lastUpdate != timeStamp) {
						lastUpdate = timeStamp;
						MomentSummary removable = transactions.get(timeStamp - 60);
						if (removable != null) {
							transactions.remove(removable);
							differenceAmount = differenceAmount - removable.getAmount();
							differenceQty = removable.getQuantity() - differenceQty;							
						}
					} else {
						transactions.get(timeStamp).add(transaction.getAmount());
					}
					transactions.putIfAbsent(timeStamp, new MomentSummary(transaction.getAmount()));
			}
			for(MomentSummary m: transactions.values()) {
				System.out.println(m.getQuantity());
			}
			synchronized (stats){
				stats.setSum(stats.getSum()+differenceAmount);
				stats.setCount(stats.getCount()+differenceQty);
				stats.setMax(transactions.entrySet().parallelStream()
								.map(t -> t.getValue().getMax())
								.max((t1,t2) -> Double.compare(t1, t2)).get());
				stats.setMin(transactions.entrySet().stream()
						.map(t -> t.getValue().getMax())
						.max( (t1,t2) -> Double.compare(t2, t1)).get());			
			}
		}else {
			synchronized(stats) {
				this.transactions.get(timeStamp).add(transaction.getAmount());
				stats.setSum(stats.getSum()+differenceAmount);
				stats.setCount(stats.getCount()+differenceQty);
				stats.setMax(transactions.entrySet().parallelStream()
								.map(t -> t.getValue().getMax())
								.max((t1,t2) -> Double.compare(t1, t2)).get());
				stats.setMin(transactions.entrySet().parallelStream()
						.map(t -> t.getValue().getMax())
						.min( (t1,t2) -> Double.compare(t2, t1)).get());
			}
		}
	}
	
	public Statistics getLastPeriodStats(long limit) {
		long currentTime = Instant.now().getEpochSecond();
		if (currentTime - 60 > lastUpdate) {
			stats.clear();
		}
		else {
			Map<Long, MomentSummary> removables = transactions.entrySet().parallelStream()
												.filter(t -> t.getKey() < limit)
												.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			synchronized(transactions){
				transactions.keySet().removeAll(removables.keySet());
			}
			long quantity = transactions.entrySet().parallelStream().mapToLong(r->r.getValue().getQuantity()).sum();
			double sum = transactions.entrySet().parallelStream().mapToDouble(r -> r.getValue().getAmount()).sum();
			double max = transactions.entrySet().parallelStream().map(t -> t.getValue().getMax()).max( (t1,t2) -> Double.compare(t1,t2)).get();
			double min = transactions.entrySet().parallelStream().map(t -> t.getValue().getMin()).min( (t1,t2) -> Double.compare(t2,t1)).get();
			synchronized(stats){
				stats.setCount(quantity);
				stats.setSum(sum);
				stats.setMax(max);
				stats.setMin(min);
			}
		}
		return stats;
	}
	
	
	
}
