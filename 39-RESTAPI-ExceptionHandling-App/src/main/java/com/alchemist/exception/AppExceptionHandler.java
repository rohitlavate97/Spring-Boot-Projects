package com.alchemist.exception;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {
	@ExceptionHandler(value=Exception.class)
	public ResponseEntity<ErrorInfo> handleException(Exception e){
		String errMsg = e.getMessage();
		ErrorInfo info = new ErrorInfo();
		info.setCode(errMsg);
		info.setMsg("SBIEX0003");
		info.setWhen(LocalDate.now());
		return new ResponseEntity<>(info,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value=UserNotFoundException.class)
	public ResponseEntity<ErrorInfo> handleUserNotFoundException(UserNotFoundException e){
		String errMsg = e.getMessage();
		ErrorInfo info = new ErrorInfo();
		info.setCode(errMsg);
		info.setMsg("SBIEX0004");
		info.setWhen(LocalDate.now());
		return new ResponseEntity<>(info,HttpStatus.BAD_REQUEST);
	}

}
