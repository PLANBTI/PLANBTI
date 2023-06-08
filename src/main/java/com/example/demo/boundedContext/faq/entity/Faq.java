package com.example.demo.boundedContext.faq.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.boundedContext.faq.dto.FaqDto;
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
        FaqDto dto = (FaqDto) o;
        return Objects.equals(title, dto.getTitle()) && Objects.equals(content, dto.getContent()) && Objects.equals(email, dto.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, email);
    }
}
