package com.example.demo.aggreagate.member.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Embeddable
public class Address {

    private String zipCode;
    private String addrDetail;
    private String addr;
    private String basic;

}
