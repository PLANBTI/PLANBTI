package com.example.demo.boundedContext.product.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
public class ShoppingBasket extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "shoppingBasket_product")
    private List<Product> products = new ArrayList<>();

    private int count;

    public void addProduct(Product product) {
        Optional<Product> optionalProduct = findProduct(product.getId());
        if (optionalProduct.isEmpty()) {
            addCount(1);
            this.products.add(product);
        }
    }

    public Optional<Product> findProduct(Long productId) {
        return products.stream().filter(i -> i.getId().equals(productId)).findFirst();
    }

    public void addCount(int count) {
        this.count += count;
    }

    public boolean isOwner(Long memberId) {
        return member.getId().equals(memberId);
    }

}
