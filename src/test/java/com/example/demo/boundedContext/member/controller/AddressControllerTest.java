package com.example.demo.boundedContext.member.controller;

import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.AddressService;
import com.example.demo.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class AddressControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    AddressService addressService;
    @Autowired
    MemberService memberService;

    @Test
    @WithUserDetails("user1")
    @DisplayName("create")
    void t001() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/address/create")
                        .with(csrf())
                        .param("name", "테스트2")
                        .param("addr", "서울시")
                        .param("addrDetail", "동작구")
                        .param("zipCode", "11111")
                        .param("phoneNumber", "01012345678"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AddressController.class))
                .andExpect(handler().methodName("create"))
                .andExpect(status().is3xxRedirection());

        Member member = memberService.findByUsernameAndDeleteDateIsNull("user1");
        List<Address> list = addressService.findByMember(member);
        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    @WithUserDetails("user1")
    @DisplayName("modify")
    void t002() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/address/modify/1")
                        .with(csrf())
                        .param("name", "테스트1 수정")
                        .param("addr", "대구시")
                        .param("addrDetail", "수성구")
                        .param("zipCode", "22222")
                        .param("phoneNumber", "01012345678")
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AddressController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(status().is3xxRedirection());

        Member member = memberService.findByUsernameAndDeleteDateIsNull("user1");
        List<Address> list = addressService.findByMember(member);

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getAddr()).isEqualTo("대구시");
        assertThat(list.get(0).getAddrDetail()).isEqualTo("수성구");
        assertThat(list.get(0).getZipCode()).isEqualTo("22222");
    }

//    @Test
//    @WithUserDetails("user1")
//    @DisplayName("delete")
//    void t003() throws Exception {
//        ResultActions resultActions = mvc
//                .perform(get("/address/delete/1"))
//                .andDo(print());
//
//        resultActions
//                .andExpect(handler().handlerType(AddressController.class))
//                .andExpect(handler().methodName("delete"))
//                .andExpect(status().is3xxRedirection());
//
//        Member member = memberService.findByUsernameAndDeleteDateIsNull("user1");
//        List<Address> list = addressService.findByMember(member);
//        assertThat(list.size()).isEqualTo(0);
//    }
}
