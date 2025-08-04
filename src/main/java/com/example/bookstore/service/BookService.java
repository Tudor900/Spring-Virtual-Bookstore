package com.example.bookstore.service;
import com.example.bookstore.entity.Book;

import java.util.List;

public interface BookService {

    Book saveBook(Book book);

    List<Book> fetchBookList();

    Book updateBook(Book book, Long bookID);

    void deleteBookById(Long bookID);

    List findByBookIDIn(List <Long> bookIDs);

    Book findByBookID(Long id);
}
