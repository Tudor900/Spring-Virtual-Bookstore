package com.example.bookstore.service;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;

import java.util.List;

public interface BookService {

    Book saveBook(Book book);

    List<Book> fetchBookList();

    Book updateBook(Book book, Long bookID);

    void deleteBookById(Long bookID);

    List findByBookIDIn(List <Long> bookIDs);

    Book findByBookID(Long id);
    List<Book> findByGenre(Long genreid);
    List<Book> findByAuthor(Long authorid);
    List<Book> findByAuthorAndGenre(Long genreid, Long authorid);
}
