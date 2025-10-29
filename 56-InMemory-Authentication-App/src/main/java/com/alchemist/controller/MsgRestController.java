package com.alchemist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgRestController {
	
	@GetMapping("/greet")
	public String greetMsg() {
		return "Good Evening--> :)";
	}
	
	@GetMapping("/welcome")
	public String welcomeMsg() {
		return "Welcome to our Application... :)";
	}
	
	@GetMapping("/contact")
	public String getContact() {
		return "Contact: +918600852978";
	}
	
	@GetMapping("/")
	public String homePage() {
		return "Hi";
	}

}
