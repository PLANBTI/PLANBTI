package com.example.demo.boundedContext.faq.controller;

import com.example.demo.boundedContext.faq.Controller.FaqController;
import com.example.demo.boundedContext.faq.Service.FaqService;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.faq.entity.FaqCategory;
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
public class FaqControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    FaqService faqService;
    @Autowired
    MemberService memberService;

    @Test
    @WithUserDetails("user1")
    @DisplayName("create")
    void t001() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/faq/create")
                        .with(csrf())
                        .param("title", "test2")
                        .param("category", String.valueOf(FaqCategory.PRODUCT))
                        .param("content", "test content2")
                        .param("email", "test1@test.com"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(FaqController.class))
                .andExpect(handler().methodName("create"))
                .andExpect(status().is3xxRedirection());

        Member member = memberService.findByUsernameAndDeleteDateIsNull("user1");
        List<Faq> list = faqService.findByMemberAndDeleteDateIsNull(member);
        assertThat(list.size()).isEqualTo(3);
    }

    @Test
    @WithUserDetails("user1")
    @DisplayName("modify")
    void t002() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/faq/modify/1")
                        .with(csrf())
                        .param("title", "문의 제목 수정")
                        .param("content", "문의 내용 수정")
                        .param("email", "test2@test.com"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(FaqController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(status().is3xxRedirection());

        Member member = memberService.findByUsernameAndDeleteDateIsNull("user1");
        List<Faq> list = faqService.findByMemberAndDeleteDateIsNull(member);
        assertThat(list.size()).isEqualTo(2);

        Faq faq = list.get(0);
        assertThat(faq.getTitle()).isEqualTo("문의 제목 수정");
        assertThat(faq.getContent()).isEqualTo("문의 내용 수정");
        assertThat(faq.getEmail()).isEqualTo("test2@test.com");
    }

    @Test
    @WithUserDetails("user1")
    @DisplayName("soft-delete")
    void t003() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/faq/delete/1"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(FaqController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(status().is3xxRedirection());
        assertThat(faqService.findAll().get(0).getDeleteDate()).isNotNull();
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("hard-delete")
    void t004() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/faq/deleteAdm/1"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(FaqController.class))
                .andExpect(handler().methodName("deleteHard"))
                .andExpect(status().is3xxRedirection());
        assertThat(faqService.findAll().size()).isEqualTo(1);
    }

}
