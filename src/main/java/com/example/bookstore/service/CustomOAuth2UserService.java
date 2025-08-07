package com.example.bookstore.service;

import com.example.bookstore.entity.Customer;
import com.example.bookstore.repository.CustomerRepository;
import com.example.bookstore.security.CustomOAuth2User;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends OidcUserService {

    private final CustomerRepository customerRepository;

    public CustomOAuth2UserService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // Call super to get the default OidcUser from the provider
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String firstName = oidcUser.getAttribute("given_name");
        String lastName = oidcUser.getAttribute("family_name");

        Optional<Customer> existingUser = customerRepository.findByEmail(email);
        System.out.println("Existing User = " + existingUser);

        Customer customer;
        if (existingUser.isPresent()) {
            customer = existingUser.get();
            customer.setFirstname(firstName);
            customer.setLastname(lastName);

        } else {
            customer = new Customer(firstName, lastName, email);
            customer.setPassword("[google login]");// Still set a dummy password if required by DB
            customer.setUniqueID(UUID.randomUUID().toString());
            customerRepository.save(customer);
        }

        // Return the custom wrapper, passing both your User entity and the original OidcUser
        return new CustomOAuth2User(customer, oidcUser);
    }
}