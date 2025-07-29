package com.example.bookstore.repository;

import com.example.bookstore.entity.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AuthorRepository extends CrudRepository <Author,Long> {
}
