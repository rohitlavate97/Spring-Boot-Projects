package com.alchemist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alchemist.binding.Address;
import com.alchemist.binding.Customer;

@RestController
public class CutstomerRestController {
	
	@GetMapping("/")
	public Customer getCustomer() {
		Address addr = new Address();
		addr.setCity("Terwad");
		addr.setState("Maharashtra");
		addr.setCountry("India");
		
		Customer customer = new Customer();
		customer.setId(1);
		customer.setName("Prime");
		customer.setEmail("prime@gmail.com");
		customer.setPhno((long)996050);
		customer.setAddr(addr);
		
		return customer;
	}
}
