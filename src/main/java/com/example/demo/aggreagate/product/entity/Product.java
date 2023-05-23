package com.example.demo.aggreagate.product.entity;

import com.example.demo.aggreagate.category.entity.Category;
import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Category category;
    private String name;

    @Column(length = 1000)
    private String content;
    private int price;
    private int salePrice;
    private int count;

}
