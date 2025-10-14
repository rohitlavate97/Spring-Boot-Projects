package com.alchemist;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.alchemist.entity.Book;
import com.alchemist.repository.BookRepository;

@SpringBootApplication
public class Application {

    private final BookRepository bookRepository;

    Application(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		BookRepository bookRepository = context.getBean(BookRepository.class);
		System.out.println("BookRepository Bean: " + bookRepository.getClass().getName());
		
		/*
		 * Book b =new Book(); b.setBookName("Java"); b.setBookPrice(500.0);
		 * bookRepository.save(b); System.out.println("Book saved with ID: " +
		 * b.getId());
		 */
		 
		
		/*
		 * bookRepository.findAll().forEach(book -> { System.out.println("Book ID: " +
		 * book.getId()); System.out.println("Book Name: " + book.getBookName());
		 * System.out.println("Book Price: " + book.getBookPrice()); });
		 */
		
		/*
		 * Optional<Book> bookById = bookRepository.findById(1);
		 * System.out.println("Book By ID 1: " + bookById.get());
		 */
		
		
		/*
		 * Book b1 =new Book(); b1.setBookName("Python"); b1.setBookPrice(300.0);
		 * 
		 * Book b2 =new Book(); b2.setBookName("Csharp"); b2.setBookPrice(300.0);
		 * 
		 * Book b3 =new Book(); b3.setBookName("Rust"); b3.setBookPrice(200.0);
		 * bookRepository.saveAll( List.of(b1,b2,b3) );
		 */
		   // 3 select and 3 insert queries will be executed
		 boolean existsById = bookRepository.existsById(2);
		 System.out.println(existsById);
		 System.out.println("-----------------");
		 
		 Optional<Book> byId = bookRepository.findById(3);
		 if(byId.isPresent()) {
			 System.out.println(byId.get());
		 }
		 System.out.println("-----------------");
		 Iterable<Book> allById = bookRepository.findAllById(List.of(7,8));
		 allById.forEach(System.out::println);
//		 ArrayList<Book> books = (ArrayList<Book>) allById;
		 
		 System.out.println("-----------------");
		 bookRepository.findAll().forEach(System.out::println);
		 System.out.println("-----------------");
		 
		 
		 if(bookRepository.existsById(6)) {   //to avoid EmptyResultDataAccessException
			 bookRepository.deleteById(6);
		 }else {
			 System.out.println("Book with ID 6 not found");
		 }
		 System.out.println("-----------------");
		 
		 List<Book> byBookPriceGreaterThan = bookRepository.findByBookPriceGreaterThan(200.0);
		 byBookPriceGreaterThan.forEach(System.out::println);
		 
		  System.out.println("-----------------");
		  System.out.println("Total number of books: " + bookRepository.count());
		  
		  System.out.println("-----------------");
		  List<Book> allBooks = bookRepository.getAllBooks();
		  allBooks.forEach(System.out::println);
		  
		  System.out.println("-----------------");
		  bookRepository.getBooks().forEach(System.out::println);
		  
		  System.out.println("-----------------");
		  bookRepository.getBookNames().forEach(System.out::println);
		  
		  System.out.println("-----------------");
		  bookRepository.getBooksGreaterThenPrice(250.0).forEach(System.out::println);

		}

}
