package com.example.demo.boundedContext.member.controller;

import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
public class MemberControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    MemberService memberService;

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("mypage")
    void t001() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/member/mypage"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showMyPage"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("modify")
    void t002() throws Exception {

        ResultActions resultActions = mvc
                .perform(post("/member/modify")
                        .with(csrf())
                        .param("email", "user1@google.com")
                        .param("phoneNumber", "010-1111-2222")
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(status().is3xxRedirection());

        Member findMember = memberService.findByUsername("user1");
        assertThat(findMember.getEmail()).isEqualTo("user1@google.com");
        assertThat(findMember.getPhoneNumber()).isEqualTo("010-1111-2222");
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("delete, 회원 탈퇴")
    void t003() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/member/delete")
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(status().is3xxRedirection());

        assertThatThrownBy(() -> memberService.findByUsernameAndDeleteDateIsNull("user1"))
                .isInstanceOf(DataNotFoundException.class);
    }
}
