package com.example.bookstore.service;

import com.example.bookstore.entity.Customer;
import com.example.bookstore.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CustomerServiceImplementation implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Customer saveCustomer(Customer customer) {
        customer.setAdmin(false);
        customer.setUniqueID(UUID.randomUUID().toString());

        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);

        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(Customer customer){
        return customerRepository.findByEmail(customer.getEmail()).orElseThrow(() -> new RuntimeException("Customer not found"));
    }
//
//
//    FIND BY + OR ELSE THROW WILL BE CHANGED SO THAT THE WEBPAGE
//    WON'T THROW A /ERROR PAGE WHEN RUNTIMEEXCEPTION HIT
//
//
//

    @Override
    public Boolean checkForCustomer(Customer customer){
        Customer selectCustomer = customerRepository.findByEmail(customer.getEmail()).orElseThrow(() -> new RuntimeException("Customer not found"));

        String givenPassword = customer.getPassword();

        return passwordEncoder.matches(givenPassword, selectCustomer.getPassword());

    }

    @Override
    public Boolean checkForCustomerByUniqueId(String uniqueID){


        return customerRepository.findByUniqueID(uniqueID).isPresent();

    }

    @Override
    public Boolean checkForAdmin(String uniqueID){
        System.out.println(uniqueID);
        Customer customer = customerRepository.findByUniqueID(uniqueID).orElseThrow(() -> new RuntimeException("Customer not found"));

        return customer.isAdmin();
    }

    // Read operation
    @Override
    public List<Customer> fetchCustomerList()
    {
        return (List<Customer>)
                customerRepository.findAll();
    }

    // Update operation
    @Override
    public Customer updateCustomer(Customer customer,
                     Long customerId)
    {

        Customer depDB = customerRepository.findById(customerId).get();



        return customerRepository.save(depDB);
    }

    // Delete operation
    @Override
    public void deleteCustomerById(Long customerId)
    {
        customerRepository.deleteById(customerId);
    }

}
