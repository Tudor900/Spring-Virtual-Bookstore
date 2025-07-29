package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BookRepository extends CrudRepository <Book,Long>{
}
