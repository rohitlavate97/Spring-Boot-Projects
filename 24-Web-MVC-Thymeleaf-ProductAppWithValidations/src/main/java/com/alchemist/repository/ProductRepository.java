package com.alchemist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alchemist.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

}
