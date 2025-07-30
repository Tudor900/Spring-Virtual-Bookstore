package com.example.bookstore.service;

import com.example.bookstore.entity.Author;

import com.example.bookstore.repository.AuthorRepository;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class AuthorServiceImplementation implements AuthorService{
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @Override
    public Author saveAuthor(Author author){
        return authorRepository.save(author);
    }

    @Override
    public List<Author> fetchAuthorList()
    {
        return (List<Author>)
                authorRepository.findAll();
    }

    // Update operation
    @Override
    public Author updateAuthor(Author author,
                                   Long authorId)
    {

        Author depDB = authorRepository.findById(authorId).get();



        return authorRepository.save(depDB);
    }

    // Delete operation
    @Override
    public void deleteAuthorById(Long authorId)
    {
        authorRepository.deleteById(authorId);
    }
    

}
