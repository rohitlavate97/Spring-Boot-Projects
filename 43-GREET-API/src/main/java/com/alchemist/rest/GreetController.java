package com.alchemist.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetController {
	
	@GetMapping("/greet")
	public String getGreetMsg() {
		return "Good Morning";
	}

}
