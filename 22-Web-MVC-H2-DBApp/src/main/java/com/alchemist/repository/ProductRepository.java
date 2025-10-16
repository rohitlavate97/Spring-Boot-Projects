package com.alchemist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alchemist.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{

}
