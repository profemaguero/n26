package com.n26.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.n26.statistics.entities",
							   "com.n26.statistics.controllers"
								})
public class N26CodeChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(N26CodeChallengeApplication.class, args);
	}
}
