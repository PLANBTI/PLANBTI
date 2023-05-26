package com.example.demo.boundedContext.product.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
public class WishList extends BaseEntity {

    @ManyToOne
    private Member member;

    @ManyToOne
    private Product product;

}
