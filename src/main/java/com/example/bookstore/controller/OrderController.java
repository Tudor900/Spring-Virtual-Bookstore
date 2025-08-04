package com.example.bookstore.controller;

import com.example.bookstore.entity.Order;
import com.example.bookstore.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping ("/order")
    public List<Order> fetchOrderList()
    {
        return orderService.fetchOrderList();
    }
}
