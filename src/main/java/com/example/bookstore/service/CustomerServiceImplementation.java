package com.example.bookstore.service;

import com.example.bookstore.entity.Customer;
import com.example.bookstore.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
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


       String encodedPassword = passwordEncoder.encode(customer.getPassword());
       customer.setPassword(encodedPassword);

       return customerRepository.save(customer);
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
