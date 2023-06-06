package com.example.demo.boundedContext.member.entity;

import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
        Address address = (Address) o;
        return this.getName().equals(address.getName()) && this.getAddr().equals(address.getAddr()) && this.getAddrDetail().equals(address.getAddrDetail()) &&
                this.getZipCode().equals(address.getZipCode()) && this.getPhoneNumber().equals(address.getPhoneNumber()) && this.isDefault == address.isDefault;
    }
}
