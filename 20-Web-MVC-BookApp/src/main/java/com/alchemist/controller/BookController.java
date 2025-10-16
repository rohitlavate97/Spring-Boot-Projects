package com.alchemist.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alchemist.entity.Book;
import com.alchemist.repository.BookRepository;

@Controller
public class BookController {
	
	@Autowired
	private BookRepository bookRepository;
	@GetMapping("/book")
	public ModelAndView getBookById(@RequestParam("id") Integer id) {
		ModelAndView modelAndView =new ModelAndView();
		Optional<Book> byId = bookRepository.findById(id);
		if(byId.isPresent()) {
		Book bookObj = byId.get();
		modelAndView.addObject("book", bookObj);
		}
		modelAndView.setViewName("index");
		return modelAndView;	
	}

}
