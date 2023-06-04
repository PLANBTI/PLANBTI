package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.OrderException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.order.dto.LastOrderDto;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.event.ProductDecreaseEvent;
import com.example.demo.util.rq.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher publisher;


    public void verifyRequest(OrderRequest orderRequest, Long memberId) {

        verify(orderRequest,memberId);

        orderPayComplete(orderRequest);
    }

    @Transactional
    public void verify(OrderRequest orderRequest, Long memberId) {
        Order order = findByOrderRequest(orderRequest);

        if (!isOrderOwner(memberId, order))
            throw new OrderException("해당 주문에 권한 없음");

        order.canOrder(orderRequest);
    }


    private Order findByOrderRequest(OrderRequest orderRequest) {

        Optional<Order> optionalOrder = orderRepository.findByUuid(orderRequest.getOrderId());

        if (optionalOrder.isEmpty())
            throw new OrderException("존재하지 않는 주문입니다");

        return optionalOrder.get();
    }

    private boolean isOrderOwner(Long memberId, Order order) {
        return order.getMember().getId().equals(memberId);
    }


    @Transactional
    public void orderPayComplete(OrderRequest orderRequest) {
        Order order = findByOrderRequest(orderRequest);

        order.updateComplete();
        List<OrderDetail> orderDetailList = order.getOrderDetailList();

        for (OrderDetail orderDetail : orderDetailList) {
            publisher.publishEvent(new ProductDecreaseEvent(orderDetail.getProduct().getId(),orderDetail.getCount()));
        }
    }

    public ResponseData<LastOrderDto> findLastOrderById(Long id) {

        Optional<Order> lastOrder = orderRepository.findLastOrder(id);

        return lastOrder
                .map(order -> ResponseData.of("success", "성공", new LastOrderDto(order)))
                .orElseGet(() -> ResponseData.of("fail", "실패", new LastOrderDto()));

    }

    public ResponseData<LastOrderDto> findCompleteLastOrder(Long id) {
        Optional<Order> lastOrder = orderRepository.findCompleteLastOrder(id);
        return lastOrder
                .map(order -> ResponseData.of("success", "성공", new LastOrderDto(order)))
                .orElseGet(() -> ResponseData.of("fail", "실패", new LastOrderDto()));
    }

    public List<Order> findAllByMember(Member member) {
        return orderRepository.findByMember(member);
    }
}
