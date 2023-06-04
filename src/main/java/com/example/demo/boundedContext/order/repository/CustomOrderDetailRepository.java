package com.example.demo.boundedContext.order.repository;

import com.example.demo.boundedContext.order.entity.OrderDetail;

import java.util.Optional;

public interface CustomOrderDetailRepository {
    Optional<OrderDetail> findByIdWithMemberId(Long orderId, Long orderItemId,Long memberId);
}
