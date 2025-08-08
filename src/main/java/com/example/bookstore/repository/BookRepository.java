package com.example.bookstore.repository;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface BookRepository extends CrudRepository <Book,Long>{

    List<Book>  findByBookIDIn(List<Long> bookIDs);
    List<Book> findByGenre(Genre genre);
    List<Book> findByAuthor(Author author);
    List<Book> findByGenreAndAuthor(Genre genre, Author author);
}
