package com.example.bookstore.service;
import com.example.bookstore.entity.Book;

import java.util.List;

public interface BookService {

    Book saveBook(Book book);

    List<Book> fetchBookList();

    Book updateBook(Book book, Long bookID);

    void deleteBook(Long bookID);
}
