package com.alchemist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alchemist.dtos.InterestRate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class LoanService {
	@Autowired
	private RestTemplate restTemplate;
	private static final String SERVICE_NAME = "loan-service";
	private static final String RATE_SERVICE_URL ="http://localhost:9091/rate/{type}";
	
	@CircuitBreaker(name =SERVICE_NAME, fallbackMethod = "getDefaultLoan")
	public InterestRate getAllLoansByType(String type) {
		System.out.println("*****Original Method called...*****");
		ResponseEntity<InterestRate> response = restTemplate.getForEntity(RATE_SERVICE_URL, InterestRate.class, type);
		return response.getBody();
	}
	
	public InterestRate getDefaultLoan(Exception e) {
		System.out.println("*****Fallback method called.....****");
		return new InterestRate();      //returning dummy response instead of error response to the client
	}

}
