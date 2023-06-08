package com.example.demo.boundedContext.member.controller;

import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.boundedContext.member.dto.MemberModifyDto;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.service.OrderService;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.ShoppingBasket;
import com.example.demo.boundedContext.product.service.ShoppingBasketService;
import com.example.demo.util.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private final Rq rq;

    @GetMapping("/mypage")
    public String showMyPage(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        model.addAttribute("member", member);
        return "member/myPage";
    }

    // 회원 정보 조회
    @GetMapping("/profile")
    public String showProfile(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        model.addAttribute("member", member);
        return "member/profile";
    }

    @GetMapping("/modify")
    public String modify(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        model.addAttribute("member", member);
        return "member/modify";
    }

    @PostMapping("/modify")
    public String modify(@Valid MemberModifyDto dto) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());

        if(dto.equals(member)) rq.historyBack("수정된 내용이 없습니다.");

        memberService.modify(member, dto);
        return "redirect:/member/profile";
    }

    @GetMapping("/shoppingbasket")
    public String showShoppingBasket(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
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
    public String showTestResult(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        model.addAttribute("testresults", member.getTests());
        return "member/testResult";
    }

    @GetMapping("/orderlist")
    public String showOrderlist(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        List<Order> orderList = orderService.findAllByMember(member);
        model.addAttribute("orderList", orderList);
        return "member/orderlist";
    }

    // soft-delete, 회원 탈퇴
    @GetMapping("/delete")
    public String delete() {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        memberService.delete(member);
        return "redirect:/logout";
    }

}
