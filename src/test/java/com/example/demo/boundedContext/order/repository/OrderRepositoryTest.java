package com.example.demo.boundedContext.order.repository;

import com.example.demo.base.Role;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.service.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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
        for (int i = 0; i < 5; i++) {
            orderRepository.save(Order.builder()
                    .member(member).price(i * 1000).itemCount(i).build());
        }

        Order order = orderService.findLastOrderById(member.getId());
        Assertions.assertThat(order.getPrice()).isEqualTo(4000);
    }

}