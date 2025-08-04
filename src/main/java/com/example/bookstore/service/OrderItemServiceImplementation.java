package com.example.bookstore.service;

import com.example.bookstore.entity. OrderItem;
import com.example.bookstore.entity.OrderItem;
import com.example.bookstore.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class OrderItemServiceImplementation implements OrderItemService{
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public  OrderItem saveOrderItem(OrderItem orderItem){
        return orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> fetchOrderItemList(){
        return (List< OrderItem>)orderItemRepository.findAll();
    }

    @Override
    public  OrderItem updateOrderItem(OrderItem orderItem, Long orderItemID){
        return orderItemRepository.save(orderItem);
    }

    public void deleteOrderItemById(Long orderItemID){
        orderItemRepository.deleteById(orderItemID);
    }
}
