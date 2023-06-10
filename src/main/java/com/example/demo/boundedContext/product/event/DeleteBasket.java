package com.example.demo.boundedContext.product.event;

import lombok.Getter;

@Getter
public class DeleteBasket {

    private final Long itemCount;
    private final Long memberId;

    public DeleteBasket(Long itemCount, Long memberId) {
        this.itemCount = itemCount;
        this.memberId = memberId;
    }
}
