package com.example.demo.boundedContext.order.controller;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.order.dto.LastOrderDto;
import com.example.demo.boundedContext.order.dto.OrderExchangeDto;
import com.example.demo.boundedContext.order.service.OrderDetailService;
import com.example.demo.boundedContext.order.service.OrderService;
import com.example.demo.util.rq.ResponseData;
import com.example.demo.util.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/order")
@Controller
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final OrderDetailService orderDetailService;
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

    @GetMapping("/orderInfo")
    public String orderStatus(Model model) {
        ResponseData<LastOrderDto> responseData = findLastCompleteOrderOne();
        model.addAttribute("order", responseData.getContent());
        return "order/orderInfo";

    }

    @GetMapping("/exchange/{id}")
    public String exchangeGet(Model model,
                              @PathVariable(name = "id") Long orderId,
                              @RequestParam Long orderItemId) {

        OrderExchangeDto dto = orderDetailService.findOrderDetailById(orderId, orderItemId, rq.getMemberId());

        model.addAttribute("orderDetail", dto);
        return "order/exchange";
    }

    @PostMapping("/exchange/{id}")
    public String exchangePost(
            @PathVariable(name = "id") Long orderId,
            @Valid OrderExchangeDto dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "order/exchange";
        }

        if (!dto.isReturn()) {
            orderDetailService.exchange(dto);
        } else {
            orderDetailService.returnProduct(orderId, dto);
        }

        return "redirect:/order/orderInfo";
    }

    private ResponseData<LastOrderDto> findLastCompleteOrderOne() {
        String username = rq.getUsername();
        Member member = memberService.findByUsername(username);
        return orderService.findCompleteLastOrder(member.getId());
    }

}
