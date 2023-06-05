package com.example.demo.base.event;

import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.AddressService;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterDeleteAddress extends ApplicationEvent {

    private final Member member;
    private final Address address;
    private final Address deletedAddress;

    public EventAfterDeleteAddress(Object source, Member member, Address address, Address deletedAddress) {
        super(source);
        this.member = member;
        this.address = address;
        this.deletedAddress = deletedAddress;
    }
}
