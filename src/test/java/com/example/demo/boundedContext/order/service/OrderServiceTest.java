package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.OrderException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @MockBean
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("주문 검증 성공 테스트")
    @Test
    void verifyOrderSuccess() {
        Member member = Member.builder().id(1L).build();
        Order order = Order.builder()
                .member(member)
                .build();

        order.addOrderDetail(OrderDetail.builder().product(Product.builder()
                .price(1000)
                .build()).count(2).build());

        order.addOrderDetail(OrderDetail.builder().product(Product.builder()
                .price(2000)
                .build()).count(3).build());

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderRequest orderRequest = new OrderRequest("1__2__3","1",8000L);

        assertThat(order.getTotalAmount()).isEqualTo(8000L);
        assertThat(orderService.verifyRequest(orderRequest,member.getId())).isTrue();
    }

    @DisplayName("주문 검증 실패 테스트")
    @Test
    void verifyOrderFail() {
        Member member = Member.builder().id(1L).build();
        Order order = Order.builder()
                .member(member)
                .build();

        order.addOrderDetail(OrderDetail.builder().product(Product.builder()
                .price(1000)
                .build()).count(2).build());

        order.addOrderDetail(OrderDetail.builder().product(Product.builder()
                .price(2000)
                .build()).count(3).build());

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderRequest orderRequest = new OrderRequest("1__2__3","1",1000L);

        assertThat(orderService.verifyRequest(orderRequest, member.getId())).isFalse();
    }
    @DisplayName("이미 계산한 경우 실패")
    @Test
    void verifyOrderStatusFail() {
        Member member = Member.builder().id(1L).build();
        Order order = Order.builder()
                .member(member)
                .build();


        order.payComplete();

        order.addOrderDetail(OrderDetail.builder().product(Product.builder()
                .price(1000)
                .build()).count(2).build());

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderRequest orderRequest = new OrderRequest("1__2__3","1",2000L);

        assertThatThrownBy(() ->orderService.verifyRequest(orderRequest, 2L))
                .isInstanceOf(OrderException.class);
    }

    @DisplayName("주문시 자기 소유의 order가 아닌 경우 실패")
    @Test
    void noAuthorityForOrder() {
        Member member = Member.builder().id(1L).build();
        Order order = Order.builder()
                .member(member)
                .build();

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderRequest orderRequest = new OrderRequest("1__2__3","1",1000L);

        assertThatThrownBy(() ->orderService.verifyRequest(orderRequest, 2L))
                .isInstanceOf(OrderException.class);
    }

}