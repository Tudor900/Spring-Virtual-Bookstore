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
@Table(name = "Author", schema = "BOOKSTORE_SCHEMA")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long authorID;
    private String firstname;
    private String lastname;
    @Getter @Setter
    private String nationality;

    public String getName(){
        return firstname+" " +lastname;
    }



    public void setName(String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
    }






    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> book_list = new ArrayList<>();




}
