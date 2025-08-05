package com.example.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString; // Import Lombok's ToString
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Customer", schema = "BOOKSTORE_SCHEMA")
@ToString(exclude = "orders") // Exclude the field causing the loop
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long customerID;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String address;
    @CreationTimestamp
    private Instant createdOn;
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;
    @Column(name = "UNIQUEID")
    private String uniqueID ;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}