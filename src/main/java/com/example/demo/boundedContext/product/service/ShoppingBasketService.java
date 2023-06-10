package com.example.demo.boundedContext.product.service;

import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.product.entity.Basket;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.ShoppingBasket;
import com.example.demo.boundedContext.product.repository.BasketRepository;
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
    private final BasketRepository basketRepository;

    public ShoppingBasket findById(Long id) {
        Optional<ShoppingBasket> shoppingBasket = shoppingBasketRepository.findById(id);

        if (shoppingBasket.isEmpty()) {
            throw new DataNotFoundException("존재하지 않는 장바구니입니다.");
        }

        return shoppingBasket.get();
    }

    @Transactional
    public ShoppingBasket findByMember(Long memberId) {
        Optional<ShoppingBasket> shoppingBasket = shoppingBasketRepository.findByMemberId(memberId);

        if (shoppingBasket.isEmpty()) {
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
    public ResponseData<String> addProduct(Long memberId, Long productId, int count) {

        Product product = productService.findById(productId);

        Optional<Basket> dtoOptional = basketRepository.findById(memberId);

        Basket basket = dtoOptional.orElseGet(() -> new Basket(memberId));
        basket.addProduct(product,count);
        basketRepository.save(basket);

        return ResponseData.<String>builder()
                .statusCode(SUCCESS)
                .msg("장바구니에 상품을 담았습니다.")
                .build();
    }

}
