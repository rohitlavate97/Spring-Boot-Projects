package com.alchemist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgRestController {
	
	@PostMapping("/msg")                         //we have to check through postman, browser will support only get methods
	public ResponseEntity<String> saveMsg(){
		String responseBody = "Msg saved successfully....";
		return new ResponseEntity<String>(responseBody,HttpStatus.CREATED);
	}
	
	@GetMapping("/welcome")
	public ResponseEntity<String> welcomeMsg(){
		String msg = "Welcome to all people";
		return new ResponseEntity<String>(msg,HttpStatus.OK);
	}
	
	@GetMapping("/greet")
	public String getGreeting() {
		String greet = "Good Morning, everyone";
		return greet;
	}

}
