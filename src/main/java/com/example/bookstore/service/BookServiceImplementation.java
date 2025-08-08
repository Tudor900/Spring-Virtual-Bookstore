package com.example.bookstore.service;

import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;

import com.example.bookstore.entity.Author;
import com.example.bookstore.repository.AuthorRepository;

import com.example.bookstore.entity.Genre;
import com.example.bookstore.repository.GenreRepository;

import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class BookServiceImplementation implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Book saveBook(Book book){

        long bookAuthorId = book.getAuthor().getAuthorID();
        Author finalAuthor = authorRepository.findById(bookAuthorId).orElseThrow(() -> new EntityNotFoundException("Author not found"));
        book.setAuthor(finalAuthor);

        System.out.println(book.getGenre());
        long bookGenreId = book.getGenre().getGenreId();
        Genre finalGenre = genreRepository.findById(bookGenreId).orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        book.setGenre(finalGenre);

        return bookRepository.save(book);
    }

    @Override
    public List<Book> fetchBookList()
    {
        return (List<Book>)
                bookRepository.findAll();
    }

    // Update operation
    @Override
    public Book updateBook(Book book,
                               Long bookId)
    {

        Book depDB = bookRepository.findById(bookId).get();



        return bookRepository.save(depDB);
    }

    // Delete operation
    @Override
    public void deleteBookById(Long bookId)
    {
        bookRepository.deleteById(bookId);
    }

    @Override
    public List<Book> findByBookIDIn(List <Long> bookIDs){
        return bookRepository.findByBookIDIn(bookIDs);

    }

    @Override
    public Book findByBookID(Long id){
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Genre not found"));
    }

    public List<Book> findByGenre(Long genreid){

        Genre genre = genreRepository.findById(genreid).orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        return bookRepository.findByGenre(genre);
    }

    public List<Book> findByAuthor(Long authorid){
        Author author = authorRepository.findById(authorid).orElseThrow(() -> new EntityNotFoundException("Author not found"));
        return bookRepository.findByAuthor(author);
    }

    public List<Book> findByAuthorAndGenre(Long genreid, Long authorid){
        Genre genre = genreRepository.findById(genreid).orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        Author author = authorRepository.findById(authorid).orElseThrow(() -> new EntityNotFoundException("Author not found"));
        return bookRepository.findByGenreAndAuthor(genre, author);
    }



}