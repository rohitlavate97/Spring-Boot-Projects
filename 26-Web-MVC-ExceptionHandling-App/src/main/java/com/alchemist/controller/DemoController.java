package com.alchemist.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {
	
	private Logger logger = LoggerFactory.getLogger(DemoController.class);
	
	@GetMapping("/")
	public String getMessage(Model model) {
		int i=1/0;
		model.addAttribute("msg","Hi, Hello");
		return "index";
	}
	
	@GetMapping("/greet")
	public String greetMsg(Model model) {
		String txt=null;
		txt.length();
		model.addAttribute("msg","Greetings");
		return "index"; 
	}
	
	//@ExceptionHandler(value=ArithmeticException.class)
	@ExceptionHandler(value=Exception.class)
	public String handleArithmeticException(Exception ae){
		String msg = ae.getMessage();
		logger.error(msg);          //logging Error Message
		return "errorPage";
	}
}
