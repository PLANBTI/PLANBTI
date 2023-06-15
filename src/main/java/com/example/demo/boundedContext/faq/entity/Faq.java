package com.example.demo.boundedContext.faq.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
public class Faq extends BaseEntity {

    @ManyToOne
    private Member member;

    @Column(name = "faq_category")
    @Enumerated(EnumType.STRING)
    private FaqCategory category;

    private String title;
    private String content;
    private String email;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Comment comment;

    public String getCategory() {
        return category.getCategory();
    }

}
