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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "문의사항")
@RequiredArgsConstructor
@Controller
@RequestMapping("/faq")
@PreAuthorize("isAuthenticated()")
public class FaqController {

    private final FaqService faqService;
    private final CommentService commentService;
    private final MemberService memberService;
    private final Rq rq;

    @Operation(summary = "문의 사항 리스트", description = "문의 상항들을 보여줍니다.")
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public String showFaq(Model model) {
        List<Faq> faqList = faqService.findAll();
        model.addAttribute("faqList", faqList);
        return "adm/faqList";
    }

    @GetMapping("/")
    public String showFaq2() {
        return "redirect:/adm";
    }

    @Operation(summary = "회원 문의 사항", description = "해당 회원의 문의 사항을 보여줍니다.")
    @GetMapping("/myFaq")
    public String showMyFaq(Model model) {
        Member member = memberService.findByUsername(rq.getUsername());
        List<Faq> faqList = faqService.findByMember(member);
        model.addAttribute(faqList);
        return "member/faqList";
    }

    @Operation(summary = "회원 문의 사항 상세", description = "해당 회원의 상세 문의 사항을 보여줍니다.")
    @GetMapping("/detail/{id}")
    public String showFaqDetail(@PathVariable Long id, Model model) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        Comment comment = commentService.findByFaq(faq);
        model.addAttribute("faq", faq);
        model.addAttribute("comment", comment);
        return "faq/faq";
    }

    @Operation(summary = "회원 문의 사항 생성 - GET", description = "회원 문의 사항 생성 페이지를 불러옵니다.")
    @GetMapping("/create")
    public String create() {
        return "faq/create";
    }

    @Operation(summary = "회원 문의 사항 생성 요청", description = "회원 문의 사항을 생성합니다.")
    @PostMapping("/create")
    public String create(@Valid FaqDto dto) {
        Member member = memberService.findByUsername(rq.getUsername());
        Faq faq = faqService.create(member, dto);
        return rq.redirectWithMsg("/faq/detail/%s".formatted(faq.getId()), "문의글이 작성되었습니다.");
    }

    @Operation(summary = "FAQ 수정 페이지 불러오기", description = "해당 FAQ의 수정 페이지를 불러옵니다.")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        model.addAttribute("faq", faq);
        return "faq/modify";
    }

    @Operation(summary = "FAQ 수정 요청", description = "해당 FAQ를 수정합니다.")
    @Parameter(name = "dto", description = "수정할 FAQ 정보")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, FaqModifyDto dto) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);

        if (dto.isSame(faq)) rq.historyBack("수정된 내용이 없습니다.");

        faqService.modify(faq, dto);
        return rq.redirectWithMsg("/faq/detail/%s".formatted(faq.getId()), "문의글을 수정하였습니다.");
    }

    // 회원이 자신의 FAQ를 삭제
    @Operation(summary = "회원이 자신의 FAQ 삭제", description = "회원이 자신의 FAQ를 삭제합니다.")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);

        if(!faq.getMember().getUsername().equals(rq.getUsername())) {
            return rq.historyBack("접근 권한이 없습니다.");
        }

        faqService.delete(faq);
        return rq.redirectWithMsg("/faq/myFaq", "문의글을 삭제하였습니다.");
    }

    // 관리자가 FAQ를 완전 삭제
    @Operation(summary = "관리자가 FAQ 완전 삭제", description = "관리자가 FAQ를 완전 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deleteAdm/{id}")
    public String deleteHard(@PathVariable Long id) {
        Faq faq = faqService.findById(id);
        faqService.deleteHard(faq);
        return rq.redirectWithMsg("/adm/faq", "문의글을 삭제하였습니다.");
    }

}