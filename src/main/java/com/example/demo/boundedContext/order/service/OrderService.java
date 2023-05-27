package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.OrderException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.repository.OrderRepository;
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

    public boolean verifyRequest(OrderRequest orderRequest) {

        Order order = findByOrderRequest(orderRequest);

        return order.canOrder(orderRequest);
    }

    @Transactional
    public void orderPayComplete(OrderRequest orderRequest) {
        Order order = findByOrderRequest(orderRequest);

        order.payComplete();
    }

    public Order findLastOrderById(Long id) {

        Optional<Order> lastOrder = orderRepository.findLastOrder(id);
        return lastOrder.orElseGet(() -> Order.builder().build());
    }


    public List<Order> findAllByMember(Member member) {
        return orderRepository.findByMember(member);
    }

    private Order findByOrderRequest(OrderRequest orderRequest) {

        String[] split = orderRequest.getPaymentKey().split("__");
        Long orderId = Long.parseLong(split[0]);

        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isEmpty())
            throw new OrderException("status update 에러 발생");

        return optionalOrder.get();
    }
}
