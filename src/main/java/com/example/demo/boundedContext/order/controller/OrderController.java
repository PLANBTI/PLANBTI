package com.example.demo.boundedContext.order.controller;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.order.dto.OrderResponseDto;
import com.example.demo.boundedContext.order.service.OrderService;
import com.example.demo.util.rq.ResponseData;
import com.example.demo.util.rq.Rq;
import lombok.RequiredArgsConstructor;
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
    private final Rq rq;

    @GetMapping("/orderPage")
    public String orderPage(Model model) {
        String username = rq.getUsername();
        Member member = memberService.findByUsername(username);
        ResponseData<OrderResponseDto> responseData = orderService.findLastOrderById(member.getId());

        model.addAttribute("order", responseData.getContent());
        return "order/orderPage";

    }

}
