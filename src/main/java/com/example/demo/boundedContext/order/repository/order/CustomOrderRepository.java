package com.example.demo.boundedContext.order.repository.order;

import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderStatus;

import java.util.Optional;

public interface CustomOrderRepository  {
    Optional<Order> findOrderOneByStatus(Long id, OrderStatus status);
    Optional<Order> findOrderByOrderId(Long memberId, Long orderId);
}
