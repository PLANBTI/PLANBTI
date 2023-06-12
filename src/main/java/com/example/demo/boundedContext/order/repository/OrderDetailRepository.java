package com.example.demo.boundedContext.order.repository;

import com.example.demo.boundedContext.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long>, CustomOrderDetailRepository {

}
