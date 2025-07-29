package com.example.bookstore.service;
import com.example.bookstore.entity.Author;

import java.util.List;

public interface AuthorService {

    Author saveAuthor(Author author);

    List<Author> fetchAuthorList();

    Author updateAuthor(Author author, Long authorID);

    void deleteAuthor(Long authorID);
}
