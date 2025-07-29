package com.example.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Genre", schema = "BOOKSTORE_SCHEMA")

public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long GenreId;
    @Getter @Setter
    private String genreName;



    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> book_list = new ArrayList<>();



}
