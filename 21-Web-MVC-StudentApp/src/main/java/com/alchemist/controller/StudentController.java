package com.alchemist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.alchemist.binding.Student;

@Controller
public class StudentController {
	
	@GetMapping("/")
	public String loadIndexPage(Model model) {
		Student sObj = new Student();
		model.addAttribute("student",sObj);
		return "index";
	}
	
	//Endpoint to save student record
	@PostMapping("/save")
	public String SaveStudent(Student s, Model model) {
		System.out.println(s);
		model.addAttribute("msg","Data Saved....");
		return "index";
	}

}
