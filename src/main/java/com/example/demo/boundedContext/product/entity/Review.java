package com.example.demo.boundedContext.product.entity;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
public class Review extends BaseEntity {

    @ManyToOne
    private Member member;

    @ManyToOne
    private Product product;

    private String title;

    @Column(length = 300)
    private String content;
    @Column(length = 1000)
    private String image;
    private int rate;

}
