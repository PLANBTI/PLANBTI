package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.base.exception.handler.NotOwnerException;
import com.example.demo.base.exception.handler.OrderException;
import com.example.demo.boundedContext.order.dto.OrderExchangeDto;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;

import com.example.demo.boundedContext.order.repository.orderdetail.OrderDetailRepository;
import com.example.demo.boundedContext.order.repository.order.OrderRepository;
import com.example.demo.boundedContext.product.event.ProductIncreaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.base.redis.RedisPrefix.RETURN;
import static com.example.demo.boundedContext.order.entity.OrderItemStatus.COMPLETED;
import static com.example.demo.boundedContext.order.entity.OrderItemStatus.EXCHANGE;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher publisher;

    public OrderExchangeDto findOrderDetailById(Long orderId, Long orderItemId, Long memberId) {

        return orderDetailRepository.findByOrderIdAndMemberId(orderId, orderItemId, memberId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 데이터입니다"));
    }

    public void exchange(OrderExchangeDto dto, Long memberId) {
        verify(dto.getOrderItemId(), memberId);
        OrderDetail orderDetail = orderDetailRepository.findById(dto.getOrderItemId()).orElseThrow();
        orderDetail.orderExchange();

        redisTemplate.opsForList().leftPush(EXCHANGE.name(), String.valueOf(orderDetail.getId()));
    }

    public void returnProduct(Long orderId, OrderExchangeDto dto, Long memberId) {
        verify(dto.getOrderItemId(), memberId);
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.returnProduct(dto);

        publisher.publishEvent(new ProductIncreaseEvent(dto.getProductName(), dto.getCount()));
        redisTemplate.opsForValue().set(RETURN.formatKey(memberId), order.getPaymentKey());
    }

    public void verify(Long orderItemId,Long memberId) {
        OrderDetail orderDetail = orderDetailRepository.findByIdAndMemberId(orderItemId,memberId)
                .orElseThrow(() -> new NotOwnerException("이 제품에 대한 권리가 없습니다."));

        if(orderDetail.getStatus().equals(COMPLETED)) throw new OrderException("구매 확정된 상품입니다.");
    }

}
