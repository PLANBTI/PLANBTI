package com.example.demo.boundedContext.member.service;

import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class AddressServiceTest {

    @Autowired AddressService addressService;
    @Autowired MemberService memberService;

    @Test
    @DisplayName("create")
    void t001() {
        Member user1 = memberService.findByUsernameAndDeleteDateIsNull("user1");

        Address testAddress = addressService.create(user1, "테스트 1", "대구시", "수성구", "55555", "01012345678", false);

        assertThat(user1.getAddresses().size()).isEqualTo(2);
        assertThat(testAddress.equals(user1.getAddresses().get(1))).isTrue();
    }

    @Test
    @DisplayName("modify")
    void t002() {
        Member user1 = memberService.findByUsernameAndDeleteDateIsNull("user1");

        Address testAddress = addressService.modify(user1,1L, "테스트 1", "대구시", "수성구", "55555", "01012345678", false);

        assertThat(user1.getAddresses().size()).isEqualTo(1);
        assertThat(testAddress.equals(user1.getAddresses().get(0))).isTrue();
    }

    @Test
    @DisplayName("delete")
    void t003() {
        Member user1 = memberService.findByUsernameAndDeleteDateIsNull("user1");
        Address testAddress = user1.getAddresses().get(0);

        addressService.delete(user1, testAddress);

        assertThat(user1.getAddresses().size()).isEqualTo(0);
    }
}
