package com.example.demo.boundedContext.member.service;

import com.example.demo.base.event.EventAfterCreateAddress;
import com.example.demo.base.event.EventAfterDeleteAddress;
import com.example.demo.base.event.EventAfterModifyAddress;
import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.member.controller.AddressController;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final ApplicationEventPublisher publisher;

    public Address findById(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        if (address.isEmpty()) {
            throw new DataNotFoundException("존재하지 않는 주소입니다.");
        }
        return address.get();
    }

    public Address findByIdAndDeleteDateIsNull(Long id) {
        Optional<Address> address = addressRepository.findByIdAndDeleteDateIsNull(id);
        if (address.isEmpty()) {
            throw new DataNotFoundException("존재하지 않는 주소입니다.");
        }
        return address.get();
    }

    public List<Address> findByMember(Member member) {
        return addressRepository.findByMember(member);
    }

    public Address create(Member member, String name, String addr, String addrDetail, String zipCode, String phoneNumber, boolean isDefault) {
        Address address = Address.builder()
                .member(member)
                .name(name)
                .addr(addr)
                .addrDetail(addrDetail)
                .zipCode(zipCode)
                .phoneNumber(phoneNumber)
                .isDefault(isDefault)
                .build();
        addressRepository.save(address);

        publisher.publishEvent(new EventAfterCreateAddress(this, member, address));

        return address;
    }

    public Address modify(Member member, Address address, AddressController.AddressForm form, boolean isDefault) {
        if(!address.getMember().equals(member)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근 권한이 없습니다.");
        }

        Address modifiedAddress = address.toBuilder()
                .name(form.getName())
                .addr(form.getAddr())
                .addrDetail(form.getAddrDetail())
                .zipCode(form.getZipCode())
                .phoneNumber(form.getPhoneNumber())
                .isDefault(isDefault)
                .build();

        if(address.equals(modifiedAddress)) {
            return null;
        }

        publisher.publishEvent(new EventAfterModifyAddress(this, member, address, modifiedAddress));
        addressRepository.save(modifiedAddress);
        return modifiedAddress;
    }

    // soft-delete
    public void delete(Member member, Address address) {
        if(!address.getMember().equals(member)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근 권한이 없습니다.");
        }

        Address deletedAddress = address.toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();

        publisher.publishEvent(new EventAfterDeleteAddress(this, member, address));
        addressRepository.save(deletedAddress);
    }
}