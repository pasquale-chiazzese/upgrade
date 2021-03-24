package com.pc.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * I hate seeing the Whitelabel Error Page.
 * copied from https://www.baeldung.com/spring-boot-custom-error-page
 *
 */
@Controller
public class GeneralErrorController implements ErrorController  {
	private static final Logger log = LoggerFactory.getLogger(GeneralErrorController.class);
	
    @RequestMapping("/error")
    public String handleErrors(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	log.error("Bad request to API code: {}", status);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
        
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error-404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error-500";
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}