package com.example.demo.boundedContext.order.service;

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

}