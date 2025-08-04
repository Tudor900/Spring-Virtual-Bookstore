package com.example.bookstore.repository;

import com.example.bookstore.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface OrderRepository extends CrudRepository<Order,Long> {
}
