package com.alchemist.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alchemist.binding.Book;

@RestController
public class BookRestController {
	
	@GetMapping(
			value="/book",
			produces= {"application/json","application/xml"}
			)
	public Book getBook() {
		Book b = new Book();
		b.setId(1);
		b.setName("Java");
		b.setPrice(200d);
		return b;	
	}

}
