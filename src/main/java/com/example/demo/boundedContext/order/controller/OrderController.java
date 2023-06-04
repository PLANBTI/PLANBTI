package com.example.demo.boundedContext.order.controller;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.order.dto.LastOrderDto;
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
        ResponseData<LastOrderDto> responseData = orderService.findLastOrderById(member.getId());

        model.addAttribute("order", responseData.getContent());
        return "order/orderPage";

    }

    @GetMapping("/result")
    public String resultPage(Model model) {
        ResponseData<LastOrderDto> responseData = findLastCompleteOrderOne();
        model.addAttribute("order", responseData.getContent());
        return "order/result";
    }

    @GetMapping("/orderStatus")
    public String orderStatus(Model model) {
        ResponseData<LastOrderDto> responseData = findLastCompleteOrderOne();
        model.addAttribute("order", responseData.getContent());
        return "product/orderHistory";

    }

    private ResponseData<LastOrderDto> findLastCompleteOrderOne() {
        String username = rq.getUsername();
        Member member = memberService.findByUsername(username);
        return orderService.findCompleteLastOrder(member.getId());
    }

}
