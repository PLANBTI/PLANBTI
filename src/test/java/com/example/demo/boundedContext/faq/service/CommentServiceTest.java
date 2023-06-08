package com.example.demo.boundedContext.faq.service;

import com.example.demo.boundedContext.faq.Service.CommentService;
import com.example.demo.boundedContext.faq.Service.FaqService;
import com.example.demo.boundedContext.faq.dto.FaqDto;
import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.faq.entity.FaqCategory;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
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
public class CommentServiceTest {

    @Autowired CommentService commentService;
    @Autowired FaqService faqService;
    @Autowired MemberService memberService;

    @Test
    @DisplayName("create")
    void t001() {
        Member user1 = memberService.findByUsernameAndDeleteDateIsNull("user1");
        FaqDto dto = new FaqDto("상품 관련 문의", "문의 제목2", "문의 내용2", user1.getEmail());
        Faq faq = faqService.create(user1, dto);
        commentService.create(faq, "테스트 코멘트2");

        assertThat(commentService.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("modify")
    void t002() {
        Faq faq = faqService.findAll().get(0);
        Comment comment = commentService.findAll().get(0);
        commentService.modify(faq, comment, "테스트 코멘트1 수정");

        assertThat(commentService.findAll().get(0).getContent()).isEqualTo("테스트 코멘트1 수정");
    }

    @Test
    @DisplayName("delete")
    void t03() {
        Faq faq = faqService.findAll().get(0);
        Comment comment = commentService.findAll().get(0);
        commentService.delete(faq, comment);

        assertThat(commentService.findAll().size()).isEqualTo(0);
    }

}
