package com.example.demo.boundedContext.order.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.boundedContext.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
public class OrderDetail extends BaseEntity {

    @ManyToOne
    private Product product;

    @JsonBackReference
    @ManyToOne
    private Order order;

    private int count;

    public int getAmount() {
        return product.getPrice() * count;
    }
}
