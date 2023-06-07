package com.example.demo.boundedContext.order.service;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.dto.ProductOrderDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class ProductFacadeTest {

    @Autowired
    ProductFacade facade;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderRepository orderRepository;

    @DisplayName("단일 주문 성공")
    @Test
    void t1() {
        Product product = productRepository.save(Product.builder()
                .count(10)
                .price(15000)
                .name("product1").build());

        Member user = Member.builder()
                .build();
        memberRepository.save(user);

        ProductOrderDto dto = new ProductOrderDto();
        dto.setId(product.getId());
        int count = 5;
        dto.setCount(count);

        facade.createOrderOne(dto,user.getId());
        Order order = orderRepository.findByMember(user).get(0);

        Assertions.assertThat(order.getItemCount()).isEqualTo(count);
        Assertions.assertThat(order.getTotalPrice()).isEqualTo((long) product.getPrice() * count);

        Product saveProduct = order.getOrderDetailList().get(0).getProduct();
        Assertions.assertThat(saveProduct.getId()).isEqualTo(product.getId());

    }

}