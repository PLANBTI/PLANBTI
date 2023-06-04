package com.example.demo.boundedContext.product.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductDecreaseEvent {
    private final Long productId;
    private final int count;
}
