package com.example.demo.boundedContext.order.controller;

import com.example.demo.base.security.CustomOAuth2User;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/order")
@Controller
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;

    @Value("${toss.secretKey}")
    public String secretKey;

    @GetMapping("/orderPage")
    public String orderPage(Model model, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        String username = customOAuth2User.getUsername();
        Member member = memberService.findByUsername(username);
        Order order = orderService.findLastOrderById(member.getId());

        model.addAttribute("order",order);

        return "order/orderPage";
    }

}
