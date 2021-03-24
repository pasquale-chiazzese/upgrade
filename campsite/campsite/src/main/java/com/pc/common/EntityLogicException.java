package com.pc.common;

public class EntityLogicException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String debugDetails;
	
	public EntityLogicException(String message) {
		super(message);
	}
	
	public EntityLogicException(String message, String debugDetails) {
		super(message);
		this.debugDetails = debugDetails;
	}

	public String getDebugDetails() {
		return debugDetails;
	}
	
}
