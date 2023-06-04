package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.OrderException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.order.dto.LastOrderDto;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.infra.TossPaymentInfra;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
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
    private final ProductRepository productRepository;
    private final TossPaymentInfra tossPaymentInfra;

    @Transactional
    public void verifyAndRequestToss(OrderRequest orderRequest, Long memberId) {

        Order order = findByOrderRequest(orderRequest);

        if (!isOrderOwner(memberId, order))
            throw new OrderException("해당 주문에 권한 없음");

        if (!order.canOrder(orderRequest))
            throw new OrderException("총합 계산이 맞지 않습니다.");

        orderPayComplete(orderRequest);
        tossPaymentInfra.requestPermitToss(orderRequest);
    }

    private Order findByOrderRequest(OrderRequest orderRequest) {

        Optional<Order> optionalOrder = orderRepository.findByUuid(orderRequest.getOrderId());

        if (optionalOrder.isEmpty())
            throw new OrderException("status update 에러 발생");


        return optionalOrder.get();
    }

    private static boolean isOrderOwner(Long memberId, Order order) {
        return order.getMember().getId().equals(memberId);
    }

    @Transactional
    public void orderPayComplete(OrderRequest orderRequest) {
        Order order = findByOrderRequest(orderRequest);

        order.getOrderDetailList().forEach(i -> {
            Long id = i.getProduct().getId();
            Product product = productRepository.findByIdWithLock(id).orElseThrow();
            product.updateProductCount(i.getCount());
        });

        order.updateComplete();
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
