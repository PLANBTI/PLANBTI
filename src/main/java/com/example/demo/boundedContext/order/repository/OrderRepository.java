package com.example.demo.boundedContext.order.repository;

import com.example.demo.boundedContext.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
