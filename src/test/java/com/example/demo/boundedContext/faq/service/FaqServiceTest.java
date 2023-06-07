package com.example.demo.boundedContext.faq.service;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.boundedContext.faq.Controller.FaqController;
import com.example.demo.boundedContext.faq.Service.FaqService;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.faq.entity.FaqCategory;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class FaqServiceTest {

    @Autowired
    FaqService faqService;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("create")
    void t001() {
        Member user1 = memberService.findByUsernameAndDeleteDateIsNull("user1");
        Faq faq = faqService.create(user1, FaqCategory.PRODUCT,
                "문의 제목2", "문의 내용2", user1.getEmail());

        assertThat(faqService.findAll().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("modify")
    void t002() {
        Member user1 = memberService.findByUsernameAndDeleteDateIsNull("user1");
        Faq faq = faqService.findByMember(user1).get(0);
        FaqController.FaqForm form = new FaqController.FaqForm();
        form.setTitle("수정 제목1");
        form.setContent("수정 내용1");
        form.setEmail(user1.getEmail());

        Faq modifiedFaq = faqService.modify(faq, form);

        assertThat(faq.getTitle()).isEqualTo("수정 제목1");
        assertThat(faq.getContent()).isEqualTo("수정 내용1");
    }

    @Test
    @DisplayName("soft-delete")
    void t03() {
        Member user1 = memberService.findByUsernameAndDeleteDateIsNull("user1");
        Faq faq = faqService.findByMember(user1).get(0);
        faqService.delete(faq);
        assertThatThrownBy(() -> faqService.findByIdAndDeleteDateIsNull(faq.getId())).isInstanceOf(DataNotFoundException.class);
    }

    @Test
    @DisplayName("hard-delete")
    void t004() {
        Member user1 = memberService.findByUsernameAndDeleteDateIsNull("user1");
        Faq faq = faqService.findByMember(user1).get(0);
        faqService.deleteHard(faq);
        assertThat(faqService.findAll().size()).isEqualTo(1);
    }

}
