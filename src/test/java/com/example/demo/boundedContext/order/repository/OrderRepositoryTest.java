package com.example.demo.boundedContext.order.repository;

import com.example.demo.base.Role;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.dto.OrderRequestDto;
import com.example.demo.boundedContext.order.dto.OrderExchangeDto;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.entity.OrderStatus;
import com.example.demo.boundedContext.order.service.OrderService;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    PasswordEncoder passwordEncoder;

    Member member;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        String encode = passwordEncoder.encode("1111");
        member = Member.builder()
                .username("user2")
                .password(encode)
                .phoneNumber("010-1111-1111")
                .email("user2@naver.com")
                .build();
        member.addRole(Role.USER);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("findLastOrderById test")
    void t1() {

        Order order1 = orderRepository.save(Order.builder().status(OrderStatus.COMPLETE)
                .member(member).build());

        Order order2 = orderRepository.save(Order.builder()
                .member(member).build());

        Order order3 = orderRepository.save(Order.builder().status(OrderStatus.COMPLETE)
                .member(member).build());

        OrderRequestDto order = orderService.findLastOrderByStatus(member.getId(),OrderStatus.BEFORE).getContent();
        Assertions.assertThat(order.getOrderId()).isEqualTo(order2.getId());
    }

    @Test
    @DisplayName("findByIdWithMemberId test")
    void t3() {

        Member member = memberRepository.save(Member.builder().build());

        Order order = orderRepository.save(Order.builder().status(OrderStatus.COMPLETE)
                .member(member).build());

        Product product = productRepository.save(Product.builder().name("product").count(100).price(1000).build());

        OrderDetail orderDetail = OrderDetail.builder().count(10).build();
        orderDetail.addOrder(order,product);
        orderDetailRepository.save(orderDetail);

        OrderExchangeDto dto = orderDetailRepository.findByOrderIdAndMemberId(order.getId(), orderDetail.getId(), member.getId()).orElseThrow();

        Assertions.assertThat(dto.getTotalPrice()).isEqualTo(product.getPrice() * orderDetail.getCount());
        Assertions.assertThat(dto.getOrderItemId()).isEqualTo(orderDetail.getId());
        Assertions.assertThat(dto.getProductName()).isEqualTo("product");


    }


}