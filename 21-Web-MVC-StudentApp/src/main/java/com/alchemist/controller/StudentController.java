package com.alchemist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.alchemist.binding.Student;
import com.alchemist.service.StudentService;

@Controller
public class StudentController {
	@Autowired
	private StudentService service;
	
	@GetMapping("/")
	public String loadIndexPage(Model model) {
		init(model);
		return "index";
	}
	
	//@InitBinder-->not working
	@ModelAttribute
	public void init(Model model) {
		model.addAttribute("student",new Student());
		model.addAttribute("courses", service.getCourses());
		model.addAttribute("prefTimings", service.getTimings());
	}
	
	//Endpoint to save student record
	/*
	 * @PostMapping("/save") public String saveStudent(Student s, Model model) {
	 * service.saveStudent(s); System.out.println(s);
	 * model.addAttribute("msg","Data Saved...."); init(model);
	 * System.out.println(s); return "index"; }
	 */
	
	@PostMapping("/save")
	public String handSubmitBtm(Student s, Model model) {
		boolean isSaved = service.saveStudent(s);
		if(isSaved) {
			model.addAttribute("msg", "Data Saved....");
		}
		init(model);
		return "index";	
	}

}
