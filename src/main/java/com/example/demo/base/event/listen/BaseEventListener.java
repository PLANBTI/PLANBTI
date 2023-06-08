package com.example.demo.base.event.listen;

import com.example.demo.base.redis.MemberDtoRepository;
import com.example.demo.boundedContext.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseEventListener {

    private final MemberDtoRepository memberDtoRepository;

    @EventListener
    public void loginUser(MemberDto memberDto) {
        memberDtoRepository.save(memberDto);
    }
}
