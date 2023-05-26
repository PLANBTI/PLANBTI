package com.example.demo.boundedContext.faq.entity;

import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
public class Comment extends BaseEntity {

    @OneToOne(mappedBy = "comment")
    private Faq faq;
    private String content;
}
