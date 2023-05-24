package com.example.demo.boundedContext.product.entity;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.base.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
