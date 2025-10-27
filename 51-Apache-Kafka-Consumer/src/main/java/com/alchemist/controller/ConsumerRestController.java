package com.alchemist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alchemist.model.Customer;
import com.alchemist.service.CustomerService;

@RestController
public class ConsumerRestController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/lastCustomer")
    public Customer getLastReceivedCustomer() {
        Customer c = customerService.getLastReceivedCustomer();
        if (c == null)
            throw new RuntimeException("No message received yet from Kafka topic.");
        return c;
    }

    @GetMapping("/status")
    public String getStatus() {
        return "✅ Kafka Consumer Application is running and ready to receive messages.";
    }
}
