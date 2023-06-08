package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.base.exception.handler.NotOwnerException;
import com.example.demo.boundedContext.order.dto.OrderExchangeDto;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.repository.OrderDetailRepository;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.event.ProductIncreaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher publisher;

    public OrderExchangeDto findOrderDetailById(Long orderId, Long orderItemId, Long memberId) {

        return orderDetailRepository.findByOrderIdAndMemberId(orderId, orderItemId, memberId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 데이터입니다"));
    }

    public void exchange(OrderExchangeDto dto,Long memberId) {
        verify(dto.getOrderItemId(),memberId);

        OrderDetail orderDetail = orderDetailRepository.findById(dto.getOrderItemId()).orElseThrow();
        orderDetail.orderExchange();
    }

    public void returnProduct(Long orderId, OrderExchangeDto dto,Long memberId) {
        verify(dto.getOrderItemId(),memberId);
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.returnProduct(dto);

        publisher.publishEvent(new ProductIncreaseEvent(dto.getProductName(),dto.getCount()));
    }

    public void verify(Long orderItemId,Long memberId) {
        orderDetailRepository.findByIdAndMemberId(orderItemId,memberId)
                .orElseThrow(() -> new NotOwnerException("이 제품에 대한 권리가 없습니다."));
    }
}
