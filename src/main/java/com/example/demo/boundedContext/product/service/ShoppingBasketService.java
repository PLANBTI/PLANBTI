package com.example.demo.boundedContext.product.service;

import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.ShoppingBasket;
import com.example.demo.boundedContext.product.repository.ShoppingBasketRepository;
import com.example.demo.util.rq.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.demo.util.rq.ResponseData.Status.SUCCESS;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShoppingBasketService {

    private final ShoppingBasketRepository shoppingBasketRepository;
    private final MemberService memberService;
    private final ProductService productService;

    public ShoppingBasket findById(Long id) {
        Optional<ShoppingBasket> shoppingBasket = shoppingBasketRepository.findById(id);

        if(shoppingBasket.isEmpty()) {
            throw new DataNotFoundException("존재하지 않는 장바구니입니다.");
        }

        return shoppingBasket.get();
    }

    @Transactional
    public ShoppingBasket findByMember(Long memberId) {
        Optional<ShoppingBasket> shoppingBasket = shoppingBasketRepository.findByMemberId(memberId);

        if(shoppingBasket.isEmpty()) {
            Member member = memberService.findById(memberId);


            ShoppingBasket basket = ShoppingBasket.builder()
                    .member(member)
                    .build();
            shoppingBasketRepository.save(basket);
            return basket;
        }

        return shoppingBasket.get();
    }



    @Transactional
    public ResponseData<String> addProduct(Long memberId, Long productId) {

        Product product = productService.findById(productId);

        ShoppingBasket basket = findByMember(memberId);
        basket.addProduct(product);

        return ResponseData.<String>builder()
                .statusCode(SUCCESS)
                .msg("장바구니에 상품을 담았습니다.")
                .build();
    }
}
