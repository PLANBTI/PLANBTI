package com.example.demo.aggreagate.product.entity;

import com.example.demo.aggreagate.member.entity.Member;
import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class ShoppingBasket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "shoppingBasket_product")
    private List<Product> products = new ArrayList<>();

    private int count;

}
