package com.example.demo.boundedContext.faq.Service;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.boundedContext.faq.Controller.FaqController;
import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.faq.entity.FaqCategory;
import com.example.demo.boundedContext.faq.repository.FaqRepository;
import com.example.demo.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FaqService {

    private final FaqRepository faqRepository;

    public Faq findById(Long id) {
        return faqRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Faq not found"));
    }

    public Faq findByIdAndDeleteDateIsNull(Long id) {
        return faqRepository.findByIdAndDeleteDateIsNull(id).orElseThrow(() -> new DataNotFoundException("Faq not found"));
    }

    public List<Faq> findAll() {
        return faqRepository.findAll();
    }

    public List<Faq> findByMember(Member member) {
        return faqRepository.findByMember(member);
    }

    public Faq create(Member member, Enum category, String title, String content, String email) {
        Faq faq = Faq.builder()
                .member(member)
                .category((FaqCategory) category)
                .title(title)
                .content(content)
                .email(email).build();
        faqRepository.save(faq);
        return faq;
    }

    public Faq modify(Faq faq, FaqController.FaqForm form) {
        Faq modifiedFaq = faq.toBuilder()
                .title(form.getTitle())
                .content(form.getContent())
                .email(form.getEmail()).build();

        if(faq.equals(modifiedFaq)) return null;

        faqRepository.save(modifiedFaq);
        return modifiedFaq;
    }

    // soft-delete
    public void delete(Faq faq) {
        Faq deletedFaq = faq.toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();
        faqRepository.save(deletedFaq);
    }

    // hard-delete
    public void deleteHard(Faq faq) {
        faqRepository.delete(faq);
    }

    public void whenAfterCreateComment(Faq faq, Comment comment) {
        Faq faq1 = faq.toBuilder()
                .comment(comment).build();
        faqRepository.save(faq1);
    }

    public void whenAfterModifyComment(Faq faq, Comment modifiedComment) {
        Faq faq1 = faq.toBuilder()
                .comment(modifiedComment).build();
        faqRepository.save(faq1);
    }

    public void whenBeforeDeleteComment(Faq faq) {
        Faq faq1 = faq.toBuilder()
                .comment(null).build();
        faqRepository.save(faq1);
    }
}
