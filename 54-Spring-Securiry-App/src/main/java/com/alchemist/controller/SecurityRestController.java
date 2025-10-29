package com.alchemist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityRestController {
	@GetMapping("/hi")
	public String sayHi() {
		return "Hi, How are you?";
	}
	
	@GetMapping("/hello")
	public String sayHello() {
		return "Hello, how are you?";
	}
	
	@GetMapping("/contact")
	public String getContact() {
		return "Call us::+918600852978";
	}
}
