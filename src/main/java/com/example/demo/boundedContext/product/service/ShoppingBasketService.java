package com.example.demo.boundedContext.product.service;

import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.product.entity.ShoppingBasket;
import com.example.demo.boundedContext.product.repository.ShoppingBasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingBasketService {

    private final ShoppingBasketRepository shoppingBasketRepository;

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
}
