package com.example.demo.boundedContext.member.service;

import com.example.demo.base.event.EventAfterCreateAddress;
import com.example.demo.base.event.EventAfterDeleteAddress;
import com.example.demo.base.event.EventAfterModifyAddress;
import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.member.dto.AddressDto;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final ApplicationEventPublisher publisher;

    public Address findById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 주소지입니다."));
    }

    public Address findByIdAndDeleteDateIsNull(Long id) {
        return addressRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 주소지입니다."));
    }

    public List<Address> findByMember(Member member) {
        return addressRepository.findByMember(member);
    }

    public Address create(Member member, AddressDto dto, boolean isDefault) {
        Address address = Address.builder()
                .member(member)
                .name(dto.getName())
                .addr(dto.getAddr())
                .addrDetail(dto.getAddrDetail())
                .zipCode(dto.getZipCode())
                .phoneNumber(dto.getPhoneNumber())
                .isDefault(isDefault)
                .build();
        addressRepository.save(address);

        publisher.publishEvent(new EventAfterCreateAddress(this, member, address));
        return address;
    }

    public Address modify(Member member, Address address, AddressDto dto, boolean isDefault) {
        Address modifiedAddress = address.toBuilder()
                .name(dto.getName())
                .addr(dto.getAddr())
                .addrDetail(dto.getAddrDetail())
                .zipCode(dto.getZipCode())
                .phoneNumber(dto.getPhoneNumber())
                .isDefault(isDefault)
                .build();
        addressRepository.save(modifiedAddress);

        publisher.publishEvent(new EventAfterModifyAddress(this, member, address, modifiedAddress));
        return modifiedAddress;
    }

    public void modifyDefault(Address address) {
        Address modifiedAddress = address.toBuilder()
                .isDefault(false).build();
        addressRepository.save(modifiedAddress);
    }

    // soft-delete
    public void delete(Member member, Address address) {
        Address deletedAddress = address.toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();

        publisher.publishEvent(new EventAfterDeleteAddress(this, member, address));
        addressRepository.save(deletedAddress);
    }

    // hard-delete
    public void deleteHard(Address address) {
        addressRepository.delete(address);
    }

}