package com.example.demo.boundedContext.order.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.base.exception.handler.NotEnoughProductCount;
import com.example.demo.boundedContext.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
public class OrderDetail extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderItemStatus status = PENDING;

    private int count;

    public void addOrder(Order order,Product product) {
        if (product.getCount() <= 0 )
            throw new NotEnoughProductCount("수량이 부족합니다.");
        this.product = product;

        order.addOrderDetail(this);
        this.order = order;
    }


    public int getAmount() {
        return product.getPrice() * count;
    }

    public void orderComplete() {
        updateStatus(PLACED);
    }

    public boolean isBeforePaying() {
        return status.equals(PENDING);
    }

    public void orderExchange() {
        updateStatus(EXCHANGE);
    }

    public void updateStatus(OrderItemStatus status) {
        this.status = status;
    }

    public void decreaseCount(int count) {
        this.count -= count;
    }
}
