package com.alchemist.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeRestController {
	
	
	@GetMapping("/")
	public String welcomeMsg() {
		return "Welcome to the Home Page...!";
	}
	
	@GetMapping("/greet")
	public String greetMsg() {
		return "Greeting to all of you.....!";
	}

}
