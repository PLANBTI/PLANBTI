package com.example.demo.base.event;

import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterModifyAddress extends ApplicationEvent {

    private final Member member;
    private final Address address;
    private final Address modifiedAddress;

    public EventAfterModifyAddress(Object source, Member member, Address address, Address modifiedAddress) {
        super(source);
        this.member = member;
        this.address = address;
        this.modifiedAddress = modifiedAddress;
    }
}
