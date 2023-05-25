package com.example.demo.boundedContext.member.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberModifyForm {

    @Email
    private String email;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    private String phoneNumber;

}
