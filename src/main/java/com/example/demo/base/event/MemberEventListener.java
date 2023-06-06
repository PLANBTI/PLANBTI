package com.example.demo.base.event;

import com.example.demo.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class MemberEventListener {

    private final MemberService memberService;

    @EventListener
    public void listen(EventAfterCreateAddress event) {
        memberService.whenAfterCreateAddress(event.getMember(), event.getAddress());
    }

    @EventListener
    public void listen(EventAfterModifyAddress event) {
        memberService.whenAfterModifyAddress(event.getMember(), event.getAddress(), event.getModifiedAddress());
    }

    @EventListener
    public void listen(EventAfterDeleteAddress event) {
        memberService.whenAfterDeleteAddress(event.getMember(), event.getAddress());
    }

}
