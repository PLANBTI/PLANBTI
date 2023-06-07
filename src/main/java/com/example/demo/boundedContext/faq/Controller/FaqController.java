package com.example.demo.boundedContext.faq.Controller;

import com.example.demo.boundedContext.faq.Service.CommentService;
import com.example.demo.boundedContext.faq.Service.FaqService;
import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.faq.entity.FaqCategory;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.util.rq.Rq;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
//    @PreAuthorize("hasAuthority(ADMIN)")
    public String showFaq(Model model) {
        List<Faq> faqList = faqService.findAll();
        model.addAttribute("faqList", faqList);
        return "adm/faqList";
    }

    @GetMapping("/myFaq")
    public String showMyFaq(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        List<Faq> faqList = faqService.findByMember(member);
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FaqForm {
        @NotBlank
        private String title;
        @Enumerated(EnumType.STRING)
        private FaqCategory category;
        @NotBlank
        private String content;
        @Email
        private String email;
    }

    @PostMapping("/create")
    public String create(@Valid FaqForm form) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());

        Enum category;
        if(form.getCategory().toString().equals("PRODUCT")) category = FaqCategory.PRODUCT;
        else if(form.getCategory().toString().equals("SHIPPING")) category = FaqCategory.SHIPPING;
        else if(form.getCategory().toString().equals("EXCHANGE")) category = FaqCategory.EXCHANGE;
        else if(form.getCategory().toString().equals("RETURN")) category = FaqCategory.RETURN;
        else category = FaqCategory.ETC;

        Faq faq = faqService.create(member, category, form.getTitle(), form.getContent(), form.getEmail());
        return "redirect:/faq/detail/%s".formatted(faq.getId());
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        model.addAttribute("faq", faq);
        return "faq/modify";
    }

    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, FaqForm form) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);

        if(faq.getTitle().equals(form.getTitle()) && faq.getContent().equals(form.getContent()) && faq.getEmail().equals(form.getEmail())) {
            rq.historyBack("수정된 내용이 없습니다.");
        }

        faqService.modify(faq, form.getTitle(), form.getContent(), form.getEmail());
        return "redirect:/faq/detail/%s".formatted(id);
    }

    // 회원이 자신의 FAQ를 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        faqService.delete(faq);
        return "redirect:/faq/myFaq";
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
