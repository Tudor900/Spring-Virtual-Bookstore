package com.example.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Book", schema = "BOOKSTORE_SCHEMA")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookID;
    private String title;
    private Long isbn;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @Getter
    @JsonBackReference("authorRef")
    @ToString.Exclude
    private Author author;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    @Getter
    @JsonBackReference("bookRef")
    @ToString.Exclude
    private Genre genre;





}
