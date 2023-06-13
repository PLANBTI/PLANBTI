package com.example.demo.boundedContext.member.service;

import com.example.demo.boundedContext.member.dto.AddressDto;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class AddressServiceTest {

    @Autowired AddressService addressService;
    @Autowired MemberService memberService;

    @Test
    @DisplayName("create")
    void t001() {
        Member user1 = memberService.findByUsername("user1");
        AddressDto dto = new AddressDto("테스트 1", "대구시", "수성구", "55555", "01012345678");

        Address testAddress = addressService.create(user1, dto, false);

        assertThat(user1.getAddresses().size()).isEqualTo(2);
        assertThat(testAddress.equals(user1.getAddresses().get(1))).isTrue();
    }

    @Test
    @DisplayName("modify")
    void t002() {
        Member user1 = memberService.findByUsername("user1");
        Address address = user1.getAddresses().get(0);
        AddressDto dto = new AddressDto("테스트 1", "대구시", "수성구", "55555", "01012345678");

        Address testAddress = addressService.modify(user1, address, dto, false);

        assertThat(user1.getAddresses().size()).isEqualTo(1);
        assertThat(testAddress.getName()).isEqualTo(user1.getAddresses().get(0).getName());
        assertThat(testAddress.getAddr()).isEqualTo(user1.getAddresses().get(0).getAddr());
        assertThat(testAddress.getAddrDetail()).isEqualTo(user1.getAddresses().get(0).getAddrDetail());
    }

    @Test
    @DisplayName("delete")
    void t003() {
        Member user1 = memberService.findByUsername("user1");
        Address testAddress = user1.getAddresses().get(0);

        addressService.delete(user1, testAddress);

        assertThat(user1.getAddresses().size()).isEqualTo(0);
    }
}
