package com.example.demo.boundedContext.product.entity;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE review SET delete_date = NOW() WHERE id = ?")
@SuperBuilder(toBuilder = true)
@Entity
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private String title;

    @Column(length = 300)
    private String content;
    @Column(length = 1000)
    private String image;
    private int rate;

    public void addProduct(Product product) {
        this.product = product;
    }



}
