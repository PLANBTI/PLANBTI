package com.example.demo.boundedContext.order.repository.orderdetail;

import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.repository.orderdetail.CustomOrderDetailRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long>, CustomOrderDetailRepository {

}
