package com.alchemist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MsgController {
	
	@GetMapping("/")
	public String welcomeMsg(Model model) {
		model.addAttribute("msg","Welcome to the team");
		return "index";        //Returns a Thymeleaf template view
	}
	
	@GetMapping("greet")
	@ResponseBody
	public String greetMsg() {
		return "Greetings";     //@ResponseBody returns the string directly as the HTTP response, not as a template view.
	}

}
