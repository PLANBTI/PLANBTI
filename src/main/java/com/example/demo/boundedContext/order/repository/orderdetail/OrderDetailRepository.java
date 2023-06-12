package com.example.demo.boundedContext.order.repository.orderdetail;

import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.repository.orderdetail.CustomOrderDetailRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long>, CustomOrderDetailRepository {
    List<OrderDetail> findByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
