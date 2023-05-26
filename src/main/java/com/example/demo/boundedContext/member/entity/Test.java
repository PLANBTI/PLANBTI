package com.example.demo.boundedContext.member.entity;

import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
public class Test extends BaseEntity {

    private char result;
    private String content;

    @ManyToOne
    private Member member;

}
