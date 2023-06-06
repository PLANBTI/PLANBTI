package com.example.demo.boundedContext.product.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductIncreaseEvent {
    private final String productName;
    private final int count;

}
