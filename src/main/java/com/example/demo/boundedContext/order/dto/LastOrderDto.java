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
public class LastOrderDto {

    private Long orderId;
    private LocalDateTime createDate;
    private Long totalAmount;
    private String orderName;
    private String uuid;

    private List<OrderItemDto> orderDetails = new ArrayList<>();

    public LastOrderDto(Order order) {
        this.orderId = order.getId();
        this.createDate = order.getCreateDate();
        this.totalAmount = order.getTotalPrice();
        this.orderName = order.getOrderName();
        this.uuid =order.getUuid();
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
