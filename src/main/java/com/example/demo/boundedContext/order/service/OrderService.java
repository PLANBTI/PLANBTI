package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.handler.OrderException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.dto.OrderRequestDto;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.entity.OrderStatus;
import com.example.demo.boundedContext.order.repository.OrderDetailRepository;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.dto.Basket;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.event.DeleteBasket;
import com.example.demo.boundedContext.product.event.ProductDecreaseEvent;
import com.example.demo.boundedContext.product.repository.BasketRepository;
import com.example.demo.util.rq.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.demo.util.rq.ResponseData.Status.FAIL;
import static com.example.demo.util.rq.ResponseData.Status.SUCCESS;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {
    private final BasketRepository basketRepository;

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ApplicationEventPublisher publisher;


    @Transactional
    public void verifyRequest(OrderRequest orderRequest, Long memberId) {
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
            publisher.publishEvent(new ProductDecreaseEvent(orderDetail.getProduct().getId(), orderDetail.getCount()));
        }

        publisher.publishEvent(new DeleteBasket(order.getItemCount(),order.getMember().getId()));
    }

    public ResponseData<OrderRequestDto> findLastOrderByStatus(Long memberId, OrderStatus status) {

        Optional<Order> lastOrder = orderRepository.findOrderOneByStatus(memberId, status);

        return lastOrder
                .map(order -> ResponseData.of(SUCCESS, "성공", new OrderRequestDto(order)))
                .orElseGet(() -> ResponseData.of(FAIL, "실패", new OrderRequestDto()));
    }

    public ResponseData<OrderRequestDto> findOrderByOrderId(Long memberId,Long orderId) {

        Optional<Order> lastOrder = orderRepository.findOrderByOrderId(memberId,orderId);

        return lastOrder
                .map(order -> ResponseData.of(SUCCESS, "성공", new OrderRequestDto(order)))
                .orElseGet(() -> ResponseData.of(FAIL, "실패", new OrderRequestDto()));
    }

    public List<Order> findAllByMember(Member member) {
        List<Order> orderList = orderRepository.findByMember(member);
        return orderList.stream().filter(i -> !i.getStatus().equals(OrderStatus.BEFORE)).toList();
    }


    @Transactional
    public void orderProduct(Member member, Product product, String productName, int count) {

        deleteBeforeStatus(member);

        Order order = Order.builder()
                .orderName(productName)
                .member(member)
                .build();
        orderRepository.save(order);

        OrderDetail orderDetail = OrderDetail.builder()
                .count(count)
                .build();
        orderDetail.addOrder(order, product);
        orderDetailRepository.save(orderDetail);
    }

    @Transactional
    public void createBulkOrder(List<Product> products, Member member) {

        deleteBeforeStatus(member);

        Order order = Order.builder()
                .member(member)
                .build();
        orderRepository.save(order);

        Basket basket = basketRepository.findById(member.getId()).orElseThrow();

        for (Product product : products) {

            OrderDetail orderDetail = OrderDetail.builder()
                    .count(basket.getProductCount(product.getId()))
                    .build();
            orderDetail.addOrder(order,product);
            orderDetailRepository.save(orderDetail);
        }

        orderRepository.save(order);

    }


    private void deleteBeforeStatus(Member member) {
        List<Order> lists = orderRepository.findByMemberWhereStatusBefore(member.getId());
        orderRepository.deleteAll(lists);
    }

}
