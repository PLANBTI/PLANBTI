package com.example.demo.boundedContext.member.entity;

import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
public class Address extends BaseEntity {

    @ManyToOne
    private Member member;
    private String name;
    private String addr;
    private String addrDetail;
    private String zipCode;
    private String phoneNumber;
    private boolean isDefault;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(name, address.name) && Objects.equals(addr, address.addr) && Objects.equals(addrDetail, address.addrDetail) && Objects.equals(zipCode, address.zipCode) && Objects.equals(phoneNumber, address.phoneNumber);
    }
}
