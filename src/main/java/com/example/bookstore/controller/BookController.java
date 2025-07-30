package com.example.bookstore.controller;

import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;

import java.util.List;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

public class BookController {

    @Autowired private BookService bookService;

    @PostMapping("/book")
    public Book saveBook(@Valid @RequestBody Book book){
        return bookService.saveBook(book);
    }

    @GetMapping ("/book")
    public List<Book> fetchBookList()
    {
        return bookService.fetchBookList();
    }
}
