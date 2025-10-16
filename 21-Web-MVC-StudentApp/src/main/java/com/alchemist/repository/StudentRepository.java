package com.alchemist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alchemist.entity.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer>{
	
}