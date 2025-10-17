package com.alchemist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alchemist.entity.Product;
import com.alchemist.repository.ProductRepository;

@Controller
public class ProductController {
	
	@Autowired
	public ProductRepository repo;
	
	@GetMapping("/")
	public String loadForm(Model model) {
		model.addAttribute("p", new Product());   //To bind form to binding object
		return "index";
	}
	
	@PostMapping("/product")
	public String saveProduct(@Validated @ModelAttribute("p") Product p,BindingResult result,Model model) {
		if(result.hasErrors()) {
			System.out.println(p);
			return "index";
		}
		Product savedProduct = repo.save(p);
		System.out.println(savedProduct);
		if(savedProduct.getPid()!=null) {
			model.addAttribute("msg", "Product is saved in DB");
		}
		return "index";
	}
	
	@GetMapping("/products")
	public String loadAllProducts(Model model){
		List<Product> all = repo.findAll();
		model.addAttribute("list",all);
		return "data";
	}
	
	@GetMapping("/delete")
	public String deleteRecord(@RequestParam("pid")Integer pid,Model model) {
		repo.deleteById(pid);
		List<Product> all = repo.findAll();
		model.addAttribute("msg", "Product deleted Successfully");
		model.addAttribute("list",all);    //displays latest data
		return "data";
	}

}
