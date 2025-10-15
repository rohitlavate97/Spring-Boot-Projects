package com.alchemist.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alchemist.entity.OrderDetailsEntity;

public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity, Serializable> {

}
