package com.example.demo.boundedContext.product.service;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.ShoppingBasket;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import com.example.demo.util.rq.ResponseData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @DisplayName("장바구니 추가 성공")
    @Test
    void t1() {
        Member member = Member.builder().build();
        memberRepository.save(member);

        Product product = Product.builder().build();
        productRepository.save(product);

        ResponseData<String> responseData = shoppingBasketService.addProduct(member.getId(), product.getId(),1);
        assertThat(responseData.getStatusCode()).isEqualTo(ResponseData.Status.SUCCESS);
        assertThat(responseData.getMsg()).contains("장바구니에 상품을 담았습니다.");

        ShoppingBasket basket = shoppingBasketService.findByMember(member.getId());

        assertThat(basket.getMember().getId()).isEqualTo(member.getId());
    }

}