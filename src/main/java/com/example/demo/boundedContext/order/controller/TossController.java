package com.example.demo.boundedContext.order.controller;

import com.example.demo.base.exception.OrderException;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.infra.TossPaymentInfra;
import com.example.demo.boundedContext.order.service.OrderService;
import com.example.demo.util.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/toss")
@Controller
public class TossController {


    private final OrderService orderService;
    private final TossPaymentInfra tossPaymentService;
    private final Rq rq;


    @Value("${toss.secretKey}")
    public String secretKey;

    @GetMapping("/success")
    public String orderByToss(Model model,OrderRequest orderRequest) throws Exception {

        try {
            orderService.verifyRequest(orderRequest,rq.getMemberId());
            tossPaymentService.requestPermitToss(orderRequest);

            return "redirect:/order/result";

        } catch (OrderException e) {

            return "order/orderPage";
        }
    }

    @GetMapping(value = "fail")
    public String paymentResult(
            Model model,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code) throws Exception {

        model.addAttribute("code", code);
        model.addAttribute("message", message);

        return "redirect:/order/orderPage";
    }
}
