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

    @Override
    public boolean equals(Object o) {
        Address address = (Address) o;
        return Objects.equals(name, address.getName()) && Objects.equals(addr, address.getAddr()) && Objects.equals(addrDetail, address.getAddrDetail())
                && Objects.equals(zipCode, address.getZipCode()) && Objects.equals(phoneNumber, address.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, addr, addrDetail, zipCode, phoneNumber);
    }
}