package com.alchemist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MsgController {
	
	@GetMapping("/welcome")
	public ModelAndView getWelcomeMsg() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("msg", "Welcome to Alchemist World!");
		mav.setViewName("welcome");         //i.e name of the jsp file/view file/thymeleaf file
		return mav;
	}
	
	@GetMapping("/greet")
	public ModelAndView getGreeting() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("msg", "Greetings to all of the alchemists well wishers!");
		mav.setViewName("greet");
		return mav;
	}
}
