package com.pc.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pc.common.ApiError;
import com.pc.common.EntityLogicException;

/**
 * Use a simple global exception handling.
 * 
 * inspired from https://www.toptal.com/java/spring-boot-rest-api-error-handling
 * https://reflectoring.io/bean-validation-with-spring-boot/
 * 
 * https://stackoverflow.com/questions/55789337/best-practice-to-send-response-in-spring-boot
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(EntityLogicException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityLogicException ex) {
		log.error("Entity Not Found", ex);
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
		apiError.setMessage(ex.getMessage());
		if(StringUtils.isNotBlank(ex.getDebugDetails())) {
			apiError.setDebugMessage(ex.getDebugDetails());
		}
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleValidationError(MethodArgumentNotValidException ex) {
		log.error("Failed Validation", ex);
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleGeneralError(Exception ex) {
		log.error("General uncaught error", ex);
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

}
