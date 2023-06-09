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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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

        facade.createOrderOne(dto, user.getId());
        Order order = orderRepository.findByMember(user).get(0);

        assertThat(order.getItemCount()).isEqualTo(count);
        assertThat(order.getTotalPrice()).isEqualTo((long) product.getPrice() * count);

        Product saveProduct = order.getOrderDetailList().get(0).getProduct();
        assertThat(saveProduct.getId()).isEqualTo(product.getId());

    }

    @DisplayName("장바구니 여러개 주문 성공")
    @Test
    void t2() {
        List<Long> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Product product = productRepository.save(Product.builder()
                    .id((long) i)
                    .count(10)
                    .price(1000 * i)
                    .name("product"+i).build());
            list.add(product.getId());
        }

        Member user = Member.builder()
                .build();
        memberRepository.save(user);

        facade.createBulkOrder(list, user.getId());

        Order order = orderRepository.findByMember(user).get(0);

        assertThat(order.getItemCount()).isEqualTo(5);
        assertThat(order.getTotalPrice()).isEqualTo(15000);
        assertThat(order.getOrderDetailList().size()).isEqualTo(5);
    }


}