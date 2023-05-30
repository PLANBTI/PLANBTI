package com.example.demo.boundedContext.order.dto;

import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Long orderId;
    private LocalDateTime createDate;
    private Long totalAmount;

    private List<OrderItemDto> orderDetails = new ArrayList<>();

    public OrderResponseDto(Order order) {
        this.orderId = order.getId();
        createDate = order.getCreateDate();
        totalAmount = order.getTotalAmount();
        order.getOrderDetailList()
                .forEach(i -> orderDetails.add(new OrderItemDto(i.getProduct().getName(),i.getCount(),i.getAmount())));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class OrderItemDto {
        private String productName;
        private int count;
        private int amount;

        public OrderItemDto(OrderDetail orderDetail) {
            productName = orderDetail.getProduct().getName();
            count = orderDetail.getCount();
            amount = orderDetail.getAmount();

        }
    }
}
