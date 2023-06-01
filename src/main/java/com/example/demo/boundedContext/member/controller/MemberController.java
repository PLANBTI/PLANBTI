package com.example.demo.boundedContext.member.controller;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.base.security.CustomOAuth2User;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.form.MemberModifyForm;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.service.OrderService;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.ShoppingBasket;
import com.example.demo.boundedContext.product.service.ShoppingBasketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@PreAuthorize("isAuthenticated()")
public class MemberController {
    private final MemberService memberService;
    private final OrderService orderService;
    private final ShoppingBasketService shoppingBasketService;

    @GetMapping("/mypage")
    public String showMyPage(Model model, @AuthenticationPrincipal CustomOAuth2User user) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(user.getName());

        model.addAttribute("member", member);
        return "member/myPage";
    }

    // 회원 정보 조회
    @GetMapping("/profile")
    public String showProfile(Model model, @AuthenticationPrincipal CustomOAuth2User user) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(user.getName());

        model.addAttribute("member", member);
        return "member/profile";
    }

    @GetMapping("/modify")
    public String modify(Model model, @AuthenticationPrincipal CustomOAuth2User user) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(user.getName());

        model.addAttribute("member", member);
        return "member/modify";
    }

    @PostMapping("/modify")
    public String modify(@Valid MemberModifyForm form, BindingResult bindingResult,
                         @AuthenticationPrincipal CustomOAuth2User user) {
        if(bindingResult.hasErrors()) return "member/modify";

        Member member = memberService.findByUsernameAndDeleteDateIsNull(user.getName());
        memberService.modify(member, form.getEmail(), form.getPhoneNumber());
        return "redirect:/member/profile";
    }

    @GetMapping("/shoppingbasket")
    public String showShoppingBasket(Model model, @AuthenticationPrincipal CustomOAuth2User user) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(user.getName());
        try {
            ShoppingBasket shoppingBasket = shoppingBasketService.findByMember(member);
            List<Product> products = shoppingBasket.getProducts();
            model.addAttribute("products", products);
        } catch (DataNotFoundException e) {
            model.addAttribute("products", new ArrayList<Product>());
        }
        return "member/shoppingbasket";
    }

    @GetMapping("/testresult")
    public String showTestResult(Model model, @AuthenticationPrincipal CustomOAuth2User user) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(user.getName());

        model.addAttribute("testresults", member.getTests());
        return "member/testResult";
    }

    @GetMapping("/orderlist")
    public String showOrderlist(Model model, @AuthenticationPrincipal CustomOAuth2User user) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(user.getName());
        List<Order> orderList = orderService.findAllByMember(member);

        model.addAttribute("orderList", orderList);
        return "member/orderlist";
    }

    // soft-delete, 회원 탈퇴
    @GetMapping("/delete")
    public String delete(@AuthenticationPrincipal CustomOAuth2User user) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(user.getName());
        memberService.delete(member);
        return "redirect:/logout";
    }

}
