package com.example.demo.boundedContext.order.entity;

import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "Orders")
public class Order extends BaseEntity {

    @ManyToOne
    private Member member;
    private int price;
    private String status;
    private String req;
    private int itemCount;

    @OneToOne
    private Address address;

}
