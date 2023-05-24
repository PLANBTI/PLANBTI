package com.example.demo.aggreagate.order.entity;

import com.example.demo.aggreagate.member.entity.Address;
import com.example.demo.aggreagate.member.entity.Member;
import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "Orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;
    private int price;
    private String status;
    private String req;
    private int itemCount;

    @OneToOne
    private Address address;

}
