package com.example.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString; // Import Lombok's ToString
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;



import java.time.Instant;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Customer", schema = "BOOKSTORE_SCHEMA")
@ToString(exclude = "orders") // Exclude the field causing the loop
public class Customer implements UserDetails, OAuth2User {
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

    public Customer(String firstname, String lastname, String email, String password, String address) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public Customer(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    // --- OAuth2User interface methods ---
    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("email", this.email);
        attrs.put("given_name", this.firstname);
        attrs.put("family_name", this.lastname);
        return attrs;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Or define roles/authorities
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getName() {
        return this.email;
    }

    // Getter for the 'id' field
    public Long getId() { // Explicitly added for clarity, though Lombok generates it
        return this.customerID;
    }

    // Setter for the 'id' field
    public void setId(Long id) { // Explicitly added for clarity, though Lombok generates it
        this.customerID = customerID;
    }
}