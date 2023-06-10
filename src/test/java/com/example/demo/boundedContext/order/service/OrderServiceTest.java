package com.example.demo.boundedContext.order.service;

import com.example.demo.base.exception.handler.OrderException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.entity.OrderStatus;
import com.example.demo.boundedContext.order.infra.TossPaymentInfra;
import com.example.demo.boundedContext.order.repository.OrderDetailRepository;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    TossPaymentInfra tossPaymentInfra;

    Member member;
    Order order;
    Product product;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @BeforeEach
    void setUp() {

        member = Member.builder().build();
        memberRepository.save(member);

        product = productRepository.save(Product.builder().count(100)
                .price(1000)
                .salePrice(1000)
                .build());

        Product product2 = productRepository.save(Product.builder().count(100)
                .price(2000)
                .salePrice(2000)
                .build());

        order = orderRepository.save(Order.builder()
                .member(member)
                .build());

        OrderDetail orderDetail1 = OrderDetail.builder().count(2).build();
        orderDetail1.addOrder(order, product);
        orderDetailRepository.save(orderDetail1);


        OrderDetail orderDetail2 = OrderDetail.builder().count(3).build();
        orderDetail2.addOrder(order, product2);
        orderDetailRepository.save(orderDetail2);
    }

    @DisplayName("주문 검증 성공 테스트")
    @Test
    void verifyOrderSuccess1() {

        OrderRequest orderRequest = new OrderRequest("1__2__3", order.getUuid(), 8000L);
        orderService.verifyRequest(orderRequest,member.getId());
        assertThat(order.getTotalPrice()).isEqualTo(8000L);

    }

    @DisplayName("주문 상태 Before -> Complete 성공 테스트")
    @Test
    void verifyOrderSuccess2() {

        OrderRequest orderRequest = new OrderRequest("1__2__3", order.getUuid(), 8000L);

        orderService.orderPayComplete(orderRequest);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETE);

    }

    @DisplayName("총합이 비교 실패 테스트")
    @Test
    void verifyOrderFail() {

        OrderRequest orderRequest = new OrderRequest("1__2__3", order.getUuid(), 1000L);


        Assertions.assertThatThrownBy(() -> orderService.verifyRequest(orderRequest, member.getId()))
                .isInstanceOf(OrderException.class);
    }

    @DisplayName("이미 계산한 경우 실패")
    @Test
    void verifyOrderStatusFail() {

        OrderRequest orderRequest = new OrderRequest("1__2__3", order.getUuid(), 2000L);


        assertThatThrownBy(() -> orderService.verifyRequest(orderRequest, member.getId()))
                .isInstanceOf(OrderException.class);
    }

    @DisplayName("주문시 자기 소유의 order가 아닌 경우 실패")
    @Test
    void noAuthorityForOrder() {
        OrderRequest orderRequest = new OrderRequest("1__2__3", "12123", 1000L);


        assertThatThrownBy(() -> orderService.verifyRequest(orderRequest, member.getId()))
                .isInstanceOf(OrderException.class);
    }

    @DisplayName("주문시 재고가 부족한 경우 실패")
    @Test
    void notEnoughProduct() {
        Order save = orderRepository.save(Order.builder()
                .member(member)
                .build());

        OrderDetail orderDetail1 = OrderDetail.builder().count(1200).build();
        orderDetail1.addOrder(save, product);
        orderDetailRepository.save(orderDetail1);

        OrderRequest orderRequest = new OrderRequest("1__2__3", save.getUuid(), product.getPrice() * 1200L);

        assertThatThrownBy(() -> orderService.orderPayComplete(orderRequest))
                .isInstanceOf(OrderException.class).hasMessageContaining("재고가 부족합니다");
    }
}