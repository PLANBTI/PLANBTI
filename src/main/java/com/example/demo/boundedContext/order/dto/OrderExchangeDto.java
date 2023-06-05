package com.example.demo.boundedContext.order.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderExchangeDto {

    @NotNull
    private Long orderItemId;

    private String productName;

    private int count;

    private int totalPrice;

    @Length(min = 5,max = 100)
    @NotBlank(message = "최소 5자 이상의 이유를 적어주세요")
    private String reason;


    @Pattern(regexp = "^(EXCHANGE|RETURN)$",message = "잘못된 요청입니다.")
    private String exchange;

    public OrderExchangeDto(Long orderItemId, String productName, int count, int price) {
        this.orderItemId = orderItemId;
        this.productName = productName;
        this.count = count;
        this.totalPrice = price * count;
    }

    public boolean isReturn() {
        return exchange.equals(RETURN.name());
    }
}
