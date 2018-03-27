package com.n26.statistics.controllers;

import java.time.Instant;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.statistics.entities.RealTimeStats;
import com.n26.statistics.entities.Statistics;
import com.n26.statistics.entities.Transaction;

@RestController
public class StatisticsController {

	@Autowired
	RealTimeStats realTimeStats;
	
		
	private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/statistics", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Statistics> home(Locale locale, Model model) {
		return ResponseEntity.ok(realTimeStats.getLastPeriodStats(Instant.now().getEpochSecond()-60));
	}
	
	@RequestMapping(value="/transactions", method= RequestMethod.POST, consumes="application/json")
	public ResponseEntity<?> test(@RequestBody Transaction transaction){
		long currentTS = Instant.now().getEpochSecond(); 
		if (isOutOfTimeSpan(currentTS, transaction)) {
			logger.info(String.format("Transaction from %s for %s, rejected at %s",
					                   transaction.getTimestamp(), transaction.getAmount(), currentTS));
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		realTimeStats.update(transaction);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	private boolean isOutOfTimeSpan(long timeStamp, Transaction transaction) {
		return ( timeStamp - 60) > transaction.getTimestamp();
	}
	
}
