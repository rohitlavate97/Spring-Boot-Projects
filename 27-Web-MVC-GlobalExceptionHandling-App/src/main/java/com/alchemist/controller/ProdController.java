package com.alchemist.controller;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alchemist.service.ProductService;

@Controller
@ResponseBody
public class ProdController {
	
	@Autowired
	private ProductService service;
	
	@GetMapping("/product")
	public String getProduct() {
		String name = service.getProductNameById(100);
		return name;
	}
}
