package com.example.demo.boundedContext.order.entity;

import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.base.entity.BaseEntity;
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
@Table(name = "Orders",indexes = {@Index(name = "toss_uuid",columnList = "uuid")})
public class Order extends BaseEntity {

    @ManyToOne
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
    }

    public boolean canOrder(OrderRequest orderRequest) {
        return !isPaid() && orderRequest.getAmount().equals(totalPrice);
    }

    public boolean isPaid() {
        return status.equals(OrderStatus.COMPLETE);
    }


    public void payComplete() {
        status = OrderStatus.COMPLETE;
    }

    public void addPrice(int amount) {
        this.totalPrice += amount;
    }

    public void addCount(int count) {
        this.itemCount += count;
    }
}
