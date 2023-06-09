package com.example.demo.boundedContext.order.repository;

import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderStatus;

import java.util.Optional;

public interface CustomOrderRepository  {
    Optional<Order> findOrderOneByStatus(Long id, OrderStatus status);
}
