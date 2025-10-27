package com.alchemist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alchemist.model.Customer;
import com.alchemist.service.CustomerService;

@RestController
public class CustomerRestController {
	@Autowired
	private CustomerService service;
	
	@PostMapping(value = "/addCustomer")
	public String addCustomer(@RequestBody List<Customer> customers) {
		return service.add(customers);
	}
}
