package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.boundedContext.order.dto.OrderExchangeDto;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.repository.OrderDetailRepository;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.EXCHANGE;


@RequiredArgsConstructor
@Transactional
@Service
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderExchangeDto findOrderDetailById(Long orderId, Long orderItemId, Long memberId) {

        return orderDetailRepository.findByOrderIdAndMemberId(orderId, orderItemId, memberId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 데이터입니다"));
    }

    public void exchange(OrderExchangeDto dto) {
        OrderDetail orderDetail = orderDetailRepository.findById(dto.getOrderItemId()).orElseThrow();
        orderDetail.updateStatus(EXCHANGE);
    }

    public void returnProduct(Long orderId, OrderExchangeDto dto) {

        Order order = orderRepository.findById(orderId).orElseThrow();
        order.returnProduct(dto);

        Product product = productRepository.findByName(dto.getProductName())
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 데이터입니다"));

        product.addCount(dto.getCount());
    }
}
