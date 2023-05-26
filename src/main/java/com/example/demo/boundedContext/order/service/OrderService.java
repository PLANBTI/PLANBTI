package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.OrderException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;


    public boolean verifyRequest(OrderRequest orderRequest) {

        String[] split = orderRequest.getPaymentKey().split("__");

        Long orderId = Long.parseLong(split[0]);
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isEmpty())
            throw new OrderException("잘못된 order 요청입니다.");

        Order order = optionalOrder.get();

        return isEqualAmount(orderRequest, order);
    }

    private boolean isEqualAmount(OrderRequest orderRequest, Order order) {
        return orderRequest.getAmount().equals(order.getTotalAmount());
    }

    public Order findLastOrderById(Long id) {

        return orderRepository.findLastOrder(id).get();
    }


    public List<Order> findAllByMember(Member member) {
        return orderRepository.findByMember(member);
    }
}
