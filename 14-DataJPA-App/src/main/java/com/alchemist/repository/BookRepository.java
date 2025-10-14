package com.alchemist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.alchemist.entity.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {
	
	public List<Book> findByBookPriceGreaterThan(Double price);
	
	//nateive query
	@Query(value="select * from book", nativeQuery=true)
	public List<Book> getAllBooks();
	
	//HQL Query
	@Query("from Book")
	public List<Book> getBooks();
	
	//Projection query
	@Query("select b.bookName from Book b")
	public List<String> getBookNames();
	
	//selection Query
	@Query("from Book where bookPrice > ?1")
	public List<Book> getBooksGreaterThenPrice(Double price);

}
