package com.example.demo.boundedContext.order.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.base.exception.NotEnoughProductCount;
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

    public void addOrder(Order order,Product product) {
        if (product.getCount() <= 0 )
            throw new NotEnoughProductCount("수량이 부족합니다.");
        this.product = product;

        order.addOrderDetail(this);
        order.addCount(count);
        order.addPrice(getAmount());
        this.order = order;
    }


    public int getAmount() {
        return product.getPrice() * count;
    }
}
