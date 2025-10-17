package com.alchemist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MsgController {
	
	@GetMapping("/welcome")
	public String welcomeMessage(Model model) {
		model.addAttribute("msg", "Welcome to Thymeleaf pages");
		return "index";	
	}
	
	@GetMapping("/greet")
	public String greetMessge(Model model) {
		model.addAttribute("msg", "Greetings for the day...");
		return "index";
	}

}
