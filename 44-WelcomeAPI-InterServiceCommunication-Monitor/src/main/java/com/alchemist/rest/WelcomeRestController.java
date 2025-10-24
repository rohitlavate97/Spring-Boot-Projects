package com.alchemist.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WelcomeRestController {
	
	@GetMapping("/welcome")
	public String welcomeMsg() {
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> forEntity = rt.getForEntity("http://localhost:9090/greet", String.class);
		String greetResponse = forEntity.getBody();
		return greetResponse + " Welcome to REST";
	}

}
