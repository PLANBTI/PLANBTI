package com.example.demo.boundedContext.order.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderExchangeDto {
    private Long orderItemId;
    private String productName;
    private int count;
    private int amount;

    public OrderExchangeDto(Long orderItemId, String productName, int count, int price) {
        this.orderItemId = orderItemId;
        this.productName = productName;
        this.count = count;
        this.amount = price * count;
    }
}
