package com.example.demo.boundedContext.member.dto;

import com.example.demo.boundedContext.member.entity.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    @NotBlank
    private String name;

    @NotBlank
    private String addr;

    @NotBlank
    private String addrDetail;

    @Size(min = 5, max = 7)
    private String zipCode;

    @NotBlank
    private String phoneNumber;

    public boolean isSame(Address address) {
        return name.equals(address.getName()) && addr.equals(address.getAddr()) && addrDetail.equals(address.getAddrDetail())
                && zipCode.equals(address.getZipCode()) && phoneNumber.equals(address.getPhoneNumber());
    }
}