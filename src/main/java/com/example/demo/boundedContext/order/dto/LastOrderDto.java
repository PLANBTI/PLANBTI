package com.example.demo.boundedContext.order.dto;

import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.entity.OrderItemStatus;
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
    private LocalDateTime modifyDate;
    private Long totalAmount;
    private String orderName;
    private String uuid;
    private OrderItemStatus status;

    private List<OrderItemDto> orderDetails = new ArrayList<>();

    public LastOrderDto(Order order) {
        this.orderId = order.getId();
        this.createDate = order.getCreateDate();
        this.modifyDate = order.getModifyDate();
        this.totalAmount = order.getTotalPrice();
        this.orderName = order.getOrderName();
        this.uuid =order.getUuid();
        order.getOrderDetailList()
                .forEach(i -> orderDetails.add(new OrderItemDto(i)));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class OrderItemDto {
        private Long orderItemId;
        private String productName;
        private int count;
        private int amount;
        private OrderItemStatus status;

        public OrderItemDto(OrderDetail orderDetail) {
            this.orderItemId = orderDetail.getId();
            this.productName = orderDetail.getProduct().getName();
            this.count = orderDetail.getCount();
            this.amount = orderDetail.getAmount();
            this.status = orderDetail.getStatus();
        }
    }

    public boolean isEmpty() {
        return orderId == null;
    }
}
