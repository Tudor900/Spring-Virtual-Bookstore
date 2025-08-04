package com.example.bookstore.service;
import com.example.bookstore.entity.Order;

import java.util.List;

public interface OrderService {
    Order saveOrder(Order order);

    List<Order> fetchOrderList();

    Order  updateOrder(Order order ,Long orderID);

    void deleteOrderById(Long orderID);
}
