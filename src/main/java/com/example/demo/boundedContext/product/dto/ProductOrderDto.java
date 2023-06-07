package com.example.demo.boundedContext.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ProductOrderDto {

    @NotNull
    private Long id;

    @Min(1)
    private int count;
}
