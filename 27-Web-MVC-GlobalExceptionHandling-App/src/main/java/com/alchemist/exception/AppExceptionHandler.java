package com.alchemist.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);
	
	@ExceptionHandler(value=Exception.class)
	public String handleException(Exception e) {
		String errorText = e.getMessage();
		logger.error(errorText);
		return "errorPage";		
	}
	
	@ExceptionHandler(value=NullPointerException.class)
	public String handleNullPointerEx(NullPointerException e) {
		String errorText = e.getMessage();
		logger.error(errorText);
		return "errorPage";
	}

}
