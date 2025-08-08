package com.example.bookstore.repository;

import com.example.bookstore.entity.Customer;
import com.example.bookstore.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface OrderRepository extends CrudRepository<Order,Long> {
    List<Order> findByCustomer(Customer customer);

    Order findFirstByCustomerOrderByOrderIDDesc(Customer customer);

}
