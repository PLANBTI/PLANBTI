package com.example.demo.base.adm.controller;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
//@PreAuthorize("hasAuthority(ADMIN)") 로컬 서버에서의 테스트를 위해 주석 처리
@RequestMapping("/adm")
public class admController {

    private final MemberService memberService;

    @GetMapping("")
    public String showAdmMain() {
        return "adm/main";
    }

    @GetMapping("/members")
    public String showMembers(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "adm/members";
    }

    @GetMapping("/faq")
    public String showFaq(Model model) {
        // 문의 내역 불러와서 model에 담기
        return "adm/faqList";
    }

    @GetMapping("/reviews")
    public String showReviews(Model model) {
        // 상품평 내역 불러와서 model에 담기
        return "adm/reviews";
    }

}
