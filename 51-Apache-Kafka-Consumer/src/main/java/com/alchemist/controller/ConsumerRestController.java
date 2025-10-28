package com.alchemist.controller;

import com.alchemist.model.Customer;
import com.alchemist.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConsumerRestController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/lastCustomer")
    public Customer getLastReceivedCustomer() {
        Customer c = customerService.getLastCustomer();
        if (c == null)
            throw new RuntimeException("No message received yet from Kafka topic.");
        return c;
    }

    @GetMapping("/status")
    public String getStatus() {
        return "✅ Kafka Consumer is running on port 7070";
    }
}
