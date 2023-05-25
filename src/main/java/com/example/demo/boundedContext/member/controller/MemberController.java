package com.example.demo.boundedContext.member.controller;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@PreAuthorize("isAuthenticated()")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/")
    public String showMyPage(Model model) {
        return "member/myPage";
    }

    @Data
    @AllArgsConstructor
    public class MemberModifyForm {

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;

        @NotBlank(message = "비밀번호를 한 번 더 입력해주세요.")
        private String password2;

        @Email
        private String email;

        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        private String phoneNumber;

    }

    @GetMapping("/modify/{id}")
    public String modify(Model model, @PathVariable("id") Long id) {
        Member member = memberService.findById(id);
        model.addAttribute(member);
        return "member/modify";
    }

    @PostMapping("/modify/{id}")
    public String modify(@Valid MemberModifyForm form, BindingResult bindingResult,
                         @PathVariable("id") Long id, Principal principal) {
        if(bindingResult.hasErrors()) return "member/modify";
        Member member = memberService.findById(id);
        if(!member.getUsername().equals(principal.getName())) return "member/modify";
        memberService.modify(member, form.getPassword(), form.getEmail(), form.getPhoneNumber());
        return "redirect:/member";
    }

}
