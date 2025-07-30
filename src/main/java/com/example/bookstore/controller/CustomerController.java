package com.example.bookstore.controller;

import com.example.bookstore.entity.Customer;
import com.example.bookstore.service.CustomerService;

import java.util.List;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

public class CustomerController {

    @Autowired private CustomerService customerService;

    @PostMapping ("/customer")
    public Customer saveCustomer(@Valid @RequestBody Customer customer){
        return customerService.saveCustomer(customer);
    }

//    @GetMapping ("/customer")
//    public List<Customer> fetchCustomerList()
//    {
//        return customerService.fetchCustomerList();
//    }




}
