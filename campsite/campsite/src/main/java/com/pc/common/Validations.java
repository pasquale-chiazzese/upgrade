package com.pc.common;

/**
 * These validations are used in the DTO and BO, since they can be
 * instantiated outside of Spring, and using variable sustitution and
 * application.properties does not work.
 * 
 *
 */
public class Validations {
	
	public static final String DATE_REGEX = "d{4}-d{2}-d{2}";
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String EMAIL_REGEX = "[a-zA-Z0-9_!#$%&\u2019*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+";
	public static final String NAME_REGEX= "[a-zA-Z0-9]{1,}(?: [a-zA-Z0-9]+){0,2}";

	
	
	private Validations() {
	}
	
}
