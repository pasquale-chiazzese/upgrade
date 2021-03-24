package com.pc.common;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Custom error class to organize and error on an API call
 * 
 *
 */
public class ApiError {

	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS")
	private LocalDateTime timestamp = LocalDateTime.now();
	private String message = "Unexpected error";
	private String debugMessage = "Unrecognized error";

	public ApiError(HttpStatus status) {
		this.status = status;
	}

	public ApiError(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public ApiError(HttpStatus status, String message, Throwable ex) {
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}
	
}
