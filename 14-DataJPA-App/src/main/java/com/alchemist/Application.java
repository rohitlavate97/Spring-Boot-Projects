package com.alchemist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.alchemist.entity.Book;
import com.alchemist.repository.BookRepository;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		BookRepository bookRepository = context.getBean(BookRepository.class);
		System.out.println("BookRepository Bean: " + bookRepository.getClass().getName());
		/*
		 * Book b =new Book(); b.setBookName("Java"); b.setBookPrice(500.0);
		 * bookRepository.save(b); System.out.println("Book saved with ID: " +
		 * b.getId());
		 */
		
		bookRepository.findAll().forEach(book -> {
			System.out.println("Book ID: " + book.getId());
			System.out.println("Book Name: " + book.getBookName());
			System.out.println("Book Price: " + book.getBookPrice());
		});
		
		}

}
