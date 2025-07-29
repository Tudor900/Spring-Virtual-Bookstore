package com.example.bookstore.service;
import com.example.bookstore.entity.Genre;

import java.util.List;

public interface GenreService {

    Genre saveGenre(Genre genre);

    List<Genre> fetchGenreList();

    Genre updateGenre(Genre genre, Long genreID);

    void deleteGenre(Long genreID);
}
