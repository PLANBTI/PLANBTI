package com.example.demo.boundedContext.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberChangeDto {
    private Long id;
    private String username;
    private String password;
    private String email;

    public MemberChangeDto(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
