package com.alchemist.service;

import com.alchemist.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private Customer lastCustomer;

    @Override
    public void setLastCustomer(Customer c) {
        this.lastCustomer = c;
    }

    @Override
    public Customer getLastCustomer() {
        return lastCustomer;
    }
}
