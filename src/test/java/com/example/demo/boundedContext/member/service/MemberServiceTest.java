package com.example.demo.boundedContext.member.service;

import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.dto.MemberModifyDto;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        Member test1 = Member.builder()
                .username("test1")
                .password(passwordEncoder.encode("1234"))
                .email("test1@test.com")
                .phoneNumber("01012345678")
                .build();
        memberRepository.save(test1);
    }

    @Test
    @DisplayName("modify")
    void t001() {
        Member member = memberService.findByUsernameAndDeleteDateIsNull("test1");
        MemberModifyDto dto = new MemberModifyDto("modify1@test.com", "01000000000");

        memberService.modify(member, dto);

        assertThat(member.getEmail()).isEqualTo("modify1@test.com");
        assertThat(member.getPhoneNumber()).isEqualTo("01000000000");
    }

    @Test
    @DisplayName("soft delete")
    void t002() {
        Member member = memberService.findByUsernameAndDeleteDateIsNull("test1");

        memberService.delete(member);

        assertThatThrownBy(() -> memberService.findByUsernameAndDeleteDateIsNull("test1"))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    @DisplayName("event - whenAfterCreateAddress")
    void t003() {
        Member member = memberService.findByUsernameAndDeleteDateIsNull("test1");

        Address address = Address.builder()
                .name("test1").addr("서울시").addrDetail("중구").zipCode("00000")
                .phoneNumber("01012345678").isDefault(true).build();

        memberService.whenAfterCreateAddress(member, address);

        List<Address> list = member.getAddresses();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("event - whenAfterModifyAddress")
    void t004() {
        Member member = memberService.findByUsernameAndDeleteDateIsNull("test1");

        Address oldAddress = Address.builder()
                .name("test1").addr("서울시").addrDetail("중구").zipCode("00000")
                .phoneNumber("01012345678").isDefault(true).build();

        Address newAddress = Address.builder()
                .name("test1").addr("대구시").addrDetail("수성구").zipCode("11111")
                .phoneNumber("01012345678").isDefault(true).build();

        memberService.whenAfterModifyAddress(member, oldAddress, newAddress);

        List<Address> list = member.getAddresses();
        assertThat(list.get(0).getAddr()).isEqualTo("대구시");
        assertThat(list.get(0).getAddrDetail()).isEqualTo("수성구");
        assertThat(list.get(0).getZipCode()).isEqualTo("11111");
    }

    @Test
    @WithUserDetails("user1")
    @DisplayName("event - whenAfterDeleteAddress")
    void t005() {
        Member member = memberService.findByUsernameAndDeleteDateIsNull("user1");
        List<Address> list = member.getAddresses();
        Address deleteAddress = list.get(0);

        memberService.whenAfterDeleteAddress(member, deleteAddress);

        assertThat(list.size()).isEqualTo(0);
    }
}
