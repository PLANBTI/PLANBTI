package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.OrderException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.dto.OrderResponseDto;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.util.rq.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public boolean verifyRequest(OrderRequest orderRequest, Long memberId) {

        Order order = findByOrderRequest(orderRequest);

        if (isOrderOwner(memberId, order))
            throw new OrderException("해당 주문에 권한 없음");

        return order.canOrder(orderRequest);
    }

    private static boolean isOrderOwner(Long memberId, Order order) {
        return !order.getMember().getId().equals(memberId);
    }

    @Transactional
    public void orderPayComplete(OrderRequest orderRequest) {
        Order order = findByOrderRequest(orderRequest);

        order.payComplete();
    }

    public ResponseData<OrderResponseDto> findLastOrderById(Long id) {

        Optional<Order> lastOrder = orderRepository.findLastOrder(id);

        return lastOrder
                .map(order -> ResponseData.of("success", "성공", new OrderResponseDto(order)))
                .orElseGet(() -> ResponseData.of("fail", "실패", new OrderResponseDto()));

    }


    public List<Order> findAllByMember(Member member) {
        return orderRepository.findByMember(member);
    }

    private Order findByOrderRequest(OrderRequest orderRequest) {

        Optional<Order> optionalOrder = orderRepository.findByUuid(orderRequest.getOrderId());

        if (optionalOrder.isEmpty())
            throw new OrderException("status update 에러 발생");

        return optionalOrder.get();
    }
}
