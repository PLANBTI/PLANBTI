package com.example.demo.boundedContext.order.repository;

import com.example.demo.base.Role;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.dto.OrderResponseDto;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderStatus;
import com.example.demo.boundedContext.order.service.OrderService;
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
    OrderService orderService;

    @Autowired
    PasswordEncoder passwordEncoder;

    Member member;

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

        OrderResponseDto order = orderService.findLastOrderById(member.getId()).getContent();
        Assertions.assertThat(order.getOrderId()).isEqualTo(order2.getId());
    }

}