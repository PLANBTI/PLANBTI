package com.example.demo.boundedContext.order.repository;

import com.example.demo.boundedContext.order.dto.OrderExchangeDto;

import java.util.Optional;

public interface CustomOrderDetailRepository {

    Optional<OrderExchangeDto> findByOrderIdAndMemberId(Long orderId, Long orderItemId, Long memberId);
}
