package com.example.bookstore.service;

import com.example.bookstore.entity.Order;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.entity.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class OrderServiceImplementation implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order saveOrder(Order order){



        return orderRepository.save(order);
    }

    @Override
    public List<Order> fetchOrderList(){
        return (List<Order>)orderRepository.findAll();
    }

    public List<Order> fetchCustomerOrders(Customer customer){

        return orderRepository.findByCustomer(customer);
    }

    @Override
    public Order  updateOrder(Order order ,Long orderID){
        return orderRepository.save(order);
    }

    public void deleteOrderById(Long orderID){
        orderRepository.deleteById(orderID);
    }

    public Order returnLatestOrder(Customer customer){

        return orderRepository.findFirstByCustomerOrderByOrderIDDesc(customer);


    }

}
