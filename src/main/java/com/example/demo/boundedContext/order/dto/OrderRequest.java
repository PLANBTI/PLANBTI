package com.example.demo.boundedContext.order.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderRequest {
    String paymentKey;
    String orderId;
    Long amount;
}
