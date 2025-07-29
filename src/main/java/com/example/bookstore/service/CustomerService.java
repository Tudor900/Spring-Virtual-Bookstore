package com.example.bookstore.service;
import com.example.bookstore.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer saveCustomer(Customer customer);

    List<Customer> fetchCustomerList();

    Customer updateCustomer(Customer customer, Long customerID);

    void deleteCustomer(Long customerID);
}
