package com.alchemist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alchemist.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{

}
