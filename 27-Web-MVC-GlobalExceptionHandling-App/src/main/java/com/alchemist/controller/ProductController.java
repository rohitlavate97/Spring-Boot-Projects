package com.alchemist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.alchemist.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@GetMapping("/")
	public String getProduct(Model model) {
		String name = service.getProductNameById(100);
		model.addAttribute("msg", model);
		return "index";
	}
}
