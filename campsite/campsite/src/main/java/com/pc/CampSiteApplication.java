package com.pc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This application is losely based on Java and Spring and Hexagonal Architecture
 * https://reflectoring.io/spring-hexagonal/
 *
 * When defined layers of controller-service-data are separated using DTO and BO.
 * The Adapters have be reduced to simple method calls and not Commands, but
 * something to consider if ever stating another Line Of Business.
 * 
 */
@SpringBootApplication
public class CampSiteApplication {
	private static final Logger log = LoggerFactory.getLogger(CampSiteApplication.class);

	public static void main(String[] args) {
		log.info("Starting camp site");
		
		SpringApplication.run(CampSiteApplication.class, args);
	}
	
	
}
