package com.example.bookstore.service;
import com.example.bookstore.entity.OrderItem;

import java.util.List;

public interface OrderItemService {

    OrderItem saveOrderItem(OrderItem orderItem);

    List<OrderItem> fetchOrderItemList();

    OrderItem updateOrderItem(OrderItem orderItem, Long orderItemID);

    void deleteOrderItemById(Long orderItemID);
}
