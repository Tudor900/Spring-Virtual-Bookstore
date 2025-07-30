package com.example.bookstore.controller;

import com.example.bookstore.entity.Genre;
import com.example.bookstore.service.GenreService;

import java.util.List;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

public class GenreController {

    @Autowired private GenreService genreService;

    @PostMapping("/genre")
    public Genre saveGenre(@Valid @RequestBody Genre genre){
        return genreService.saveGenre(genre);
    }

    @GetMapping ("/genre")
    public List<Genre> fetchGenreList()
    {
        return genreService.fetchGenreList();
    }
}
