package com.alchemist.config;

import org.springframework.batch.item.ItemProcessor;
import com.alchemist.entity.Customer;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
        // Example transformation: capitalize first name
        customer.setFirstname(customer.getFirstname().toUpperCase());
        return customer;
    }
}
