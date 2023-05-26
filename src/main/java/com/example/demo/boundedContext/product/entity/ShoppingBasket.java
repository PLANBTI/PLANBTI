package com.example.demo.boundedContext.product.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
public class ShoppingBasket extends BaseEntity {

    @ManyToOne
    private Member member;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "shoppingBasket_product")
    private List<Product> products = new ArrayList<>();

    private int count;

}
