package com.example.demo.boundedContext.faq.Controller;

import com.example.demo.boundedContext.faq.Service.CommentService;
import com.example.demo.boundedContext.faq.Service.FaqService;
import com.example.demo.boundedContext.faq.dto.FaqDto;
import com.example.demo.boundedContext.faq.dto.FaqModifyDto;
import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.util.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/faq")
@PreAuthorize("isAuthenticated()")
public class FaqController {

    private final FaqService faqService;
    private final CommentService commentService;
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public String showFaq(Model model) {
        List<Faq> faqList = faqService.findAll();
        model.addAttribute("faqList", faqList);
        return "adm/faqList";
    }

    @GetMapping("/myFaq")
    public String showMyFaq(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        List<Faq> faqList = faqService.findByMemberAndDeleteDateIsNull(member);
        model.addAttribute(faqList);
        return "member/faqList";
    }

    @GetMapping("/detail/{id}")
    public String showFaqDetail(@PathVariable Long id, Model model) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        Comment comment = commentService.findByFaq(faq);
        model.addAttribute("faq", faq);
        model.addAttribute("comment", comment);
        return "faq/faq";
    }

    @GetMapping("/create")
    public String create() {
        return "faq/create";
    }

    @PostMapping("/create")
    public String create(@Valid FaqDto dto) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        Faq faq = faqService.create(member, dto);
        return rq.redirectWithMsg("/faq/detail/%s".formatted(faq.getId()), "FAQ가 작성되었습니다.");
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        model.addAttribute("faq", faq);
        return "faq/modify";
    }

    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, FaqModifyDto dto) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);

        if(dto.isSame(faq)) rq.historyBack("수정된 내용이 없습니다.");

        faqService.modify(faq, dto);
        return rq.redirectWithMsg("/faq/detail/%s".formatted(faq.getId()), "FAQ를 수정하였습니다.");
    }

    // 회원이 자신의 FAQ를 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        faqService.delete(faq);
        return rq.redirectWithMsg("/faq/detail/%s".formatted(faq.getId()), "FAQ를 삭제하였습니다.");
    }

    // 관리자가 FAQ를 완전 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deleteAdm/{id}")
    public String deleteHard(@PathVariable Long id) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        faqService.deleteHard(faq);
        return "redirect:/faq/faqList";
    }

}