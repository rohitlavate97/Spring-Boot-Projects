package com.alchemist.service;

import com.alchemist.model.Customer;

public interface CustomerService {
    void setLastCustomer(Customer c);
    Customer getLastCustomer();
}
