package com.example.demo.boundedContext.product.service;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.product.entity.Basket;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.ShoppingBasket;
import com.example.demo.boundedContext.product.repository.basket.BasketRepository;
import com.example.demo.boundedContext.product.repository.product.ProductRepository;
import com.example.demo.util.rq.ResponseData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ShoppingBasketServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ShoppingBasketService shoppingBasketService;

    @Autowired
    BasketRepository basketRepository;

    @Autowired
    RedisTemplate redisTemplate;

    @AfterEach
    void deleteRedis() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }

    @DisplayName("장바구니 추가 성공")
    @Test
    void t1() {
        Member member = Member.builder().build();
        memberRepository.save(member);

        Product product = Product.builder().count(2).build();
        productRepository.save(product);

        ResponseData<String> responseData = shoppingBasketService.addProduct(member.getId(), product.getId(), 1);
        assertThat(responseData.getStatusCode()).isEqualTo(ResponseData.Status.SUCCESS);
        assertThat(responseData.getMsg()).contains("장바구니에 상품을 담았습니다.");

        ShoppingBasket basket = shoppingBasketService.findByMember(member.getId());

        assertThat(basket.getMember().getId()).isEqualTo(member.getId());
    }

    @DisplayName("장바구니 재고량 초과 등록 실패")
    @Test
    void t2() {
        Member member = Member.builder().build();
        memberRepository.save(member);

        Product product = Product.builder().count(1).build();
        productRepository.save(product);

        ResponseData<String> responseData = shoppingBasketService.addProduct(member.getId(), product.getId(), 2);
        assertThat(responseData.getStatusCode()).isEqualTo(ResponseData.Status.FAIL);
        assertThat(responseData.getMsg()).contains("재고량을 초과하여 담을 수 없습니다.");
    }

    @DisplayName("장바구니 삭제 기능 성공")
    @Test
    void t3() {
        Member member = Member.builder().build();
        memberRepository.save(member);

        Product product = Product.builder().count(4).build();
        productRepository.save(product);

        Product product2 = Product.builder().count(4).build();
        productRepository.save(product2);

        shoppingBasketService.addProduct(member.getId(), product.getId(), 1);
        shoppingBasketService.addProduct(member.getId(), product2.getId(), 2);


        ResponseEntity<Void> delete = shoppingBasketService.delete(product2.getId(), member.getId());

        Basket basket = basketRepository.findById(member.getId()).orElseThrow();
        assertThat(delete.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(basket.getCount()).isEqualTo(1);
        assertThat(basket.getProducts().get(0).getId()).isEqualTo(product.getId());
    }

    @DisplayName("장바구니 삭제 기능 실패")
    @Test
    void t4() {
        Member member = Member.builder().build();
        memberRepository.save(member);

        Product product = Product.builder().build();
        productRepository.save(product);

        shoppingBasketService.addProduct(member.getId(), product.getId(), 1);

        ResponseEntity<Void> delete = shoppingBasketService.delete(product.getId() - 1, member.getId());

        assertThat(delete.getStatusCode().is4xxClientError()).isTrue();
    }

}