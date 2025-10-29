package com.alchemist.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgController {
	@GetMapping("/")
	public String welcomeMsg() {
		return "Welcome";
	}
	@GetMapping("/hi")
	public String hiMsg() {
		return "Hi";
	}
	@GetMapping("/hello")
	public String helloMsg() {
		return "Hello";
	}
}
