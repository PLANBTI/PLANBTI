package com.example.demo.boundedContext.member.controller;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.form.MemberModifyForm;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.order.entity.Order;
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
import java.util.List;

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

    @GetMapping("/modify/{id}")
    public String modify(Model model, @PathVariable("id") Long id) {
        Member member = memberService.findById(id);
        model.addAttribute(member);
        return "member/modify";
    }

    @PostMapping("/modify/{id}")
    public String modify(@Valid MemberModifyForm form, BindingResult bindingResult,
                         @PathVariable("id") Long id, Principal principal) {
        if(bindingResult.hasErrors()) return "/modify/%d".formatted(id);

        Member member = findByIdAndVerify(id, principal);

        memberService.modify(member, form.getEmail(), form.getPhoneNumber());
        return "redirect:/member/";
    }

    @GetMapping("/testresult/{id}")
    public String showTestResult(Model model, @PathVariable("id") Long id, Principal principal) {
        Member member = findByIdAndVerify(id, principal);

        model.addAttribute(member);
        return "/member/testResult";
    }

    @GetMapping("/orderlist/{id}")
    public String showOrderlist(Model model, @PathVariable("id") Long id, Principal principal) {
        Member member = findByIdAndVerify(id, principal);

        List<Order> orderList = member.getOrders();
        model.addAttribute(orderList);
        return "/member/orderlist";
    }

    private Member findByIdAndVerify(Long id, Principal principal) {
        Member member = memberService.findById(id);

        if(!member.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "권한이 없습니다.");
        }

        return member;
    }

}
