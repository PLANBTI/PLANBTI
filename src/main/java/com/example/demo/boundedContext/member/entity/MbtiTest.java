package com.example.demo.boundedContext.member.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.boundedContext.category.entity.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
public class MbtiTest extends BaseEntity {

    private String memberUsername;
    private String result;
    private String title;
    private String content;
    private String testImgUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}
