package com.example.bookstore.repository;

import com.example.bookstore.entity.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface GenreRepository extends CrudRepository <Genre,Long>{
}
