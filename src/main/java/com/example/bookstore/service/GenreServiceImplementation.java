package com.example.bookstore.service;

import com.example.bookstore.entity.Genre;
import com.example.bookstore.repository.GenreRepository;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class GenreServiceImplementation implements GenreService{

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Genre saveGenre(Genre genre){
        return genreRepository.save(genre);
    }

    @Override
    public List<Genre> fetchGenreList()
    {
        return (List<Genre>)
                genreRepository.findAll();
    }

    // Update operation
    @Override
    public Genre updateGenre(Genre genre,
                               Long genreId)
    {

        Genre depDB = genreRepository.findById(genreId).get();



        return genreRepository.save(depDB);
    }

    // Delete operation
    @Override
    public void deleteGenreById(Long genreId)
    {
        genreRepository.deleteById(genreId);
    }


}
