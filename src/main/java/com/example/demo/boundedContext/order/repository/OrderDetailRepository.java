package com.example.demo.boundedContext.order.repository;

import com.example.demo.boundedContext.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
}
