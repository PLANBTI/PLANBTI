package com.example.demo.boundedContext.order.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.base.exception.handler.OrderException;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.order.dto.OrderExchangeDto;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

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

    private String imageUrl;

    private String paymentKey;

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
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderDetail> orderDetailList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Address address;


    public void addOrderDetail(OrderDetail orderDetail) {
        if (!orderDetail.isBeforePaying()) {
            throw new OrderException("주문에 넣을 수 없습니다.");
        }

        if (orderDetailList.size() == 0)
            this.imageUrl = orderDetail.getImageUrl();

        orderDetailList.add(orderDetail);
        addPrice(orderDetail.getAmount());
        addCount(orderDetail.getCount());

        if (!StringUtils.hasText(orderName)) {
            orderName = orderDetail.getProduct().getName();
        } else {
            orderName = "%s 외 %d개".formatted(orderName,itemCount-1);
        }
    }

    private void addCount(int count) {
        this.itemCount += count;
    }

    private void addPrice(int price) {
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
        Long sum = orderDetailList.stream()
                .filter(i -> i.getStatus().equals(OrderItemStatus.PENDING))
                .map(OrderDetail::getAmount).mapToLong(i -> i).sum();
        if (!totalAmount.equals(sum))
            throw new OrderException("총합이 맞지 않습니다.");
    }


    public void updateComplete(String paymentKey) {
        this.paymentKey = paymentKey;
        this.status = OrderStatus.COMPLETE;
        for (OrderDetail item : orderDetailList) {
            item.orderComplete();
        }
    }

    public void returnProduct(OrderExchangeDto dto) {

        this.itemCount -= dto.getCount();
        this.totalPrice -= dto.getTotalPrice();
        orderDetailList.stream().filter(i -> i.getId().equals(dto.getOrderItemId()))
                .forEach( i -> {
                    i.updateStatus(OrderItemStatus.RETURN);
                    i.decreaseCount(dto.getCount());
                });
    }
}
