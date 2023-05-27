package com.example.demo.boundedContext.member.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class MemberDto {

    private Long id;
    private String username;
    private String email;
}
