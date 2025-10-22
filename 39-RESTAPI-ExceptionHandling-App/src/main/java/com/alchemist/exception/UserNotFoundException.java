package com.alchemist.exception;

public class UserNotFoundException extends Exception{
	private static final long serialVersionUID = 892744662240479766L;
	public UserNotFoundException() {
		
	}
	public UserNotFoundException(String msg) {
		super(msg);                              //RuntimeException constructor will be called
	}

}
