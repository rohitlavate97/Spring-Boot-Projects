package com.alchemist.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alchemist.binding.Student;
import com.alchemist.controller.StudentController;
import com.alchemist.entity.StudentEntity;
import com.alchemist.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository repo;
	
	public List<String> getCourses(){
		return Arrays.asList("JAVA","PYTHON","DEVOPS","AWS");
	}
	
	public List<String> getTimings(){
		return Arrays.asList("Morning","Afternoon","Evening");
	}
	
	public boolean saveStudent(Student student) {
		StudentEntity entity = new StudentEntity();
		BeanUtils.copyProperties(student, entity);
		entity.setTimings(Arrays.toString(student.getTimings()));
		System.out.println(entity);
		repo.save(entity);
		return true;
	}

}
