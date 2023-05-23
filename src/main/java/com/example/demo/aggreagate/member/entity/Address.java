package com.example.demo.aggreagate.member.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class Address {

    private String zipCode;
    private String addrDetail;
    private String addr;
    private String basic;

}
