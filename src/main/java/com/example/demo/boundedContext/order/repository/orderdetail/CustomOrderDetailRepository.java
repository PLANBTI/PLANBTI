package com.example.demo.boundedContext.order.repository.orderdetail;

import com.example.demo.boundedContext.order.dto.OrderExchangeDto;
import com.example.demo.boundedContext.order.entity.OrderDetail;

import java.util.Optional;

public interface CustomOrderDetailRepository {

    Optional<OrderExchangeDto> findByOrderIdAndMemberId(Long orderId, Long orderItemId, Long memberId);
    Optional<OrderDetail> findByIdAndMemberId(Long orderItemId, Long memberId);
}
