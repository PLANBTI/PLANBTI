package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.OrderException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;

    Member member;
    Order order;
    @BeforeEach
    void setUp() {
        Product product1 = productRepository.save(Product.builder()
                .price(1000)
                .build());
        Product product2 = productRepository.save(Product.builder()
                .price(2000)
                .build());
        member = Member.builder().build();
        memberRepository.save(member);

        order = orderRepository.save(Order.builder()
                .member(member)
                .build());
        order.addOrderDetail(OrderDetail.builder().product(product1).count(2).build());
        order.addOrderDetail(OrderDetail.builder().product(product2).count(3).build());
    }

    @DisplayName("주문 검증 성공 테스트")
    @Test
    void verifyOrderSuccess() {

        OrderRequest orderRequest = new OrderRequest("1__2__3",order.getUuid(),8000L);

        assertThat(order.getTotalAmount()).isEqualTo(8000L);
        assertThat(orderService.verifyRequest(orderRequest,member.getId())).isTrue();
    }

    @DisplayName("주문 검증 실패 테스트")
    @Test
    void verifyOrderFail() {

        OrderRequest orderRequest = new OrderRequest("1__2__3",order.getUuid(),1000L);

        assertThat(orderService.verifyRequest(orderRequest, member.getId())).isFalse();
    }
    @DisplayName("이미 계산한 경우 실패")
    @Test
    void verifyOrderStatusFail() {

        OrderRequest orderRequest = new OrderRequest("1__2__3",order.getUuid(),2000L);

        assertThatThrownBy(() ->orderService.verifyRequest(orderRequest, 2L))
                .isInstanceOf(OrderException.class);
    }

    @DisplayName("주문시 자기 소유의 order가 아닌 경우 실패")
    @Test
    void noAuthorityForOrder() {


        OrderRequest orderRequest = new OrderRequest("1__2__3","12123",1000L);

        assertThatThrownBy(() ->orderService.verifyRequest(orderRequest, 2L))
                .isInstanceOf(OrderException.class);
    }

}