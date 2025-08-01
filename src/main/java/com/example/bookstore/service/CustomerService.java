package com.example.bookstore.service;
import com.example.bookstore.entity.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    Customer saveCustomer(Customer customer);

    List<Customer> fetchCustomerList();

    Customer updateCustomer(String param, String uniqueID, Map<String, String> formData);

    Boolean checkForCustomer(Customer customer);

    Boolean checkForCustomerByUniqueId(String uniqueID);

    Customer getCustomer(Customer customer);

    Boolean checkForAdmin(String uniqueID);

    Customer getCustomerByUniqueId(String uniqueID);

    void deleteCustomerById(Long customerID);
}
