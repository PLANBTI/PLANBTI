package com.example.demo.boundedContext.member.dto;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("login")
@Getter
public class MemberDto {

    private  Long id;

    @Indexed
    private  String username;
    private  String email;
}
