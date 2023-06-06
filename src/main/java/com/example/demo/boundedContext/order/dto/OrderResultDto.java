package com.example.demo.boundedContext.order.dto;

import com.example.demo.boundedContext.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResultDto {

    private Long orderId;
    private LocalDateTime modifyDate;

    public OrderResultDto(Order order) {
        this.orderId = order.getId();
        this.modifyDate = order.getModifyDate();
    }
}
