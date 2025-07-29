package com.example.bookstore.entity;

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
    @Getter @Setter
    String title;
    @Getter @Setter
    private Long isbn;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @Getter
    private Author author;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    @Getter
    private Genre genre;





}
