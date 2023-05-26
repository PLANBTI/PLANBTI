package com.example.demo.boundedContext.category.entity;

import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String name;

}
