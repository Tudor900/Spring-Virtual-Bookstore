package com.example.bookstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Customer", schema = "BOOKSTORE_SCHEMA")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long customerID;
    private String firstname;
    private String lastname;
    private String email;
    private String password; //to be stored only encrypted
    private String address;
    @CreationTimestamp
    private Instant createdOn;
}
