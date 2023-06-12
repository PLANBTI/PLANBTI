package com.example.demo.boundedContext.product.repository;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.product.entity.ShoppingBasket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingBasketRepository extends JpaRepository<ShoppingBasket, Long> {
    Optional<ShoppingBasket> findByMemberId(Long id);

}
