package com.alchemist.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alchemist.feign.GreetFeignClient;

@RestController
public class WelcomeRestController {
	
	@Autowired
	private GreetFeignClient client;
	
	@GetMapping("/welcome")
	public String welcomeMsg() {
//		RestTemplate rt = new RestTemplate();
//		ResponseEntity<String> forEntity = rt.getForEntity("http://localhost:9090/greet", String.class);
//		String greetResponse = forEntity.getBody();
		String greetResponse = client.invokeGreetApi();
		return greetResponse + " Welcome to REST";
	}

}
