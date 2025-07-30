package com.example.bookstore.controller;

import com.example.bookstore.entity.Author;
import com.example.bookstore.service.AuthorService;

import java.util.List;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

public class AuthorController {

    @Autowired private AuthorService authorService;

    @PostMapping("/author")
    public Author saveAuthor(@Valid @RequestBody Author author){
        return authorService.saveAuthor(author);
    }

    @GetMapping ("/author")
    public List<Author> fetchAuthorList()
    {
        return authorService.fetchAuthorList();
    }
}
