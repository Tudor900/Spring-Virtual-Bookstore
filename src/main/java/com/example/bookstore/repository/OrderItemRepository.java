package com.example.bookstore.repository;

import com.example.bookstore.entity.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface OrderItemRepository extends CrudRepository<OrderItem,Long> {
}
