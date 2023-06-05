package com.example.demo.boundedContext.order.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.base.exception.OrderException;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.order.dto.OrderExchangeDto;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "Orders", indexes = {@Index(name = "toss_uuid", columnList = "uuid")})
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    private String req;

    private String orderName;

    @Builder.Default
    private Long totalPrice = 0L;

    @Builder.Default
    private Long itemCount = 0L;

    @Builder.Default
    private String uuid = UUID.randomUUID().toString();

    @Builder.Default
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.BEFORE;


    @JsonManagedReference
    @Builder.Default
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetailList = new ArrayList<>();

    @OneToOne
    private Address address;


    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetailList.add(orderDetail);
        appPrice(orderDetail.getAmount());
        addCount(orderDetail.getCount());
    }

    private void addCount(int count) {
        this.itemCount += count;
    }

    private void appPrice(int price) {
        this.totalPrice += price;
    }

    public void canOrder(OrderRequest orderRequest) {
        isPaid();
        isEqualAmount(orderRequest.getAmount());
    }

    public void isPaid() {
        if (!status.equals(OrderStatus.BEFORE))
            throw new OrderException("이미 주문 완료한 상품입니다.");
    }

    public void isEqualAmount(Long totalAmount) {
        if (!totalAmount.equals(totalPrice))
            throw new OrderException("총합이 맞지 않습니다.");
    }


    public void updateComplete() {
        status = OrderStatus.COMPLETE;
        for (OrderDetail item : orderDetailList) {
            item.orderComplete();
        }
    }

    public void returnProduct(OrderExchangeDto dto) {

        this.itemCount -= dto.getCount();
        this.totalPrice -= dto.getTotalPrice();

        for (OrderDetail orderDetail : orderDetailList) {
            if (orderDetail.getId().equals(dto.getOrderItemId())) {
                orderDetail.updateStatus(OrderItemStatus.RETURN);
                orderDetail.decreaseCount(dto.getCount());
                break;
            }
        }

    }
}
