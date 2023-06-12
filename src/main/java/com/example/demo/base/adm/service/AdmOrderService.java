package com.example.demo.base.adm.service;

import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdmOrderService {

    private final OrderRepository orderRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

}
