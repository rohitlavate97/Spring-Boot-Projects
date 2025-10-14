package com.alchemist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alchemist.entity.Employee;

public interface EmpRepository extends JpaRepository<Employee, Integer> {
	// No need to write any code here
	// All CRUD operations are provided by JpaRepository
	// You can define custom query methods if needed
	
	@Query(value = "SELECT * FROM employee order by emp_salary desc ", nativeQuery = true)
	public List<Employee> getEmployeeBySalaryDesc();
}
