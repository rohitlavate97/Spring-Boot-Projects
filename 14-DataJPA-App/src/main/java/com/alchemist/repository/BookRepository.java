package com.alchemist.repository;

import org.springframework.data.repository.CrudRepository;

import com.alchemist.entity.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {

}
