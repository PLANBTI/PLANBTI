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

import java.util.Optional;

import static com.example.demo.util.rq.ResponseData.Status.SUCCESS;

@Service
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

    public ShoppingBasket findByMember(Member member) {
        Optional<ShoppingBasket> shoppingBasket = shoppingBasketRepository.findByMember(member);

        if(shoppingBasket.isEmpty()) {
            throw new DataNotFoundException("존재하지 않는 장바구니입니다.");
        }

        return shoppingBasket.get();
    }


    //TODO 파사드
    public ResponseData<String> addProduct(Long memberId, Long productId) {

        Member member = memberService.findById(memberId);
        Product product = productService.findById(productId);
        ShoppingBasket shoppingBasket = ShoppingBasket
                .builder()
                .build();
        shoppingBasket.addProduct(member,product);

        shoppingBasketRepository.save(shoppingBasket);


        return ResponseData.<String>builder()
                .statusCode(SUCCESS)
                .msg("장바구니에 상품을 담았습니다.")
                .build();
    }
}
