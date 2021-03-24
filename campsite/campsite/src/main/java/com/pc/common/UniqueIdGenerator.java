package com.pc.common;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UniqueIdGenerator {
	private static final Logger log = LoggerFactory.getLogger(UniqueIdGenerator.class);

	/**
	 * A 32 character ID with dashes separating groups.
	 * 
	 * ex: be878f32-c0b8-4e34-b555-a2c77c2338d4
	 * 
	 * @return
	 */
	public String generateId() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * A 32 character ID with no dashes.
	 * 
	 * ex: be878f32c0b84e34b555a2c77c2338d4
	 * 
	 * @return
	 */
	public String generateIdNoDash() {
		return generateId().replaceAll("-", "");
	}
	
	/**
	 * A randomly generated character ID string of maxLenght.
	 * 
	 * @param maxLenght
	 * @return
	 */
	public String generateIdNoDash(int maxLenght) {
		return generateId().toLowerCase().replaceAll("-", "").substring(0, maxLenght);
	}
	
	public static void main(String[] args) {
		UniqueIdGenerator gen = new UniqueIdGenerator();
		log.debug(gen.generateId());
	}
}
