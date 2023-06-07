package com.example.demo.boundedContext.faq.controller;

import com.example.demo.boundedContext.faq.Controller.CommentController;
import com.example.demo.boundedContext.faq.Controller.FaqController;
import com.example.demo.boundedContext.faq.Service.CommentService;
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
public class CommentControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    CommentService commentService;
    @Autowired
    FaqService faqService;
    @Autowired
    MemberService memberService;

    @Test
    @WithUserDetails("admin")
    @DisplayName("create")
    void t001() throws Exception {
        Faq faq = faqService.findById(2L);
        ResultActions resultActions = mvc
                .perform(post("/comment/create/2")
                        .with(csrf())
                        .param("content", "test content"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("create"))
                .andExpect(status().is3xxRedirection());

        assertThat(faq.getComment()).isNotNull();
        assertThat(commentService.findAll().size()).isEqualTo(2);
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("modify")
    void t002() throws Exception {
        Faq faq = faqService.findById(1L);
        ResultActions resultActions = mvc
                .perform(post("/comment/modify/1")
                        .with(csrf())
                        .param("content", "modify test content"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(status().is3xxRedirection());

        assertThat(faq.getComment().getContent()).isEqualTo("modify test content");
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("delete")
    void t0023() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/comment/delete/1"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(status().is3xxRedirection());

        assertThat(commentService.findAll().size()).isEqualTo(0);
    }

}
