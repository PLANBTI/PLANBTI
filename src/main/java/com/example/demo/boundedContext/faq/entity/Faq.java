package com.example.demo.boundedContext.faq.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

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

    @OneToOne(cascade = CascadeType.ALL)
    private Comment comment;

    public String getCategory() {
        return category.getCategory();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faq faq = (Faq) o;
        return Objects.equals(title, faq.title) && Objects.equals(content, faq.content) && Objects.equals(email, faq.email);
    }
}
