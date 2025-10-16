package com.alchemist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.alchemist.entity.ProductEntity;
import com.alchemist.repository.ProductRepository;

@Controller
public class ProductController {
	
	@Autowired
	private ProductRepository repo;
	
	@GetMapping("/")
	public String loadForm(Model model) {
		model.addAttribute("p",new ProductEntity());
		return "index";	
	}
	
	@PostMapping("/product")
	public String handleSave(@ModelAttribute("p") ProductEntity p,Model model) {
		p = repo.save(p);
		if(p.getPid()!=null) {
		model.addAttribute("msg","Data Saved....");
		}
		return "index";
	}
	
/*
 * @GetMapping("/products") public String loadProducts(Model model) {
 * model.addAttribute("products",repo.findAll()); return "data"; } }
 */

	@GetMapping("/products")
	public String loadProducts(Model model) {
	    List<ProductEntity> products = repo.findAll();
	    System.out.println("Products found: " + products.size()); // Debug line
	    System.out.println("Products: " + products); // Debug line
	    model.addAttribute("products", products);
	    return "data";
	}
}