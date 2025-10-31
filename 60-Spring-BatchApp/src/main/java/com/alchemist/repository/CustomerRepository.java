package com.alchemist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alchemist.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}
