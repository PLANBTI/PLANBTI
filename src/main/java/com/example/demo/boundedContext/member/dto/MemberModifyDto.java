package com.example.demo.boundedContext.member.dto;

import com.example.demo.boundedContext.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberModifyDto {

    @Email
    private String email;

    @NotBlank
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        Member member = (Member) o;
        return Objects.equals(email, member.getEmail()) && Objects.equals(phoneNumber, member.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, phoneNumber);
    }
}
