package com.example.demo.boundedContext.order.controller;

import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.infra.TossPaymentInfra;
import com.example.demo.boundedContext.order.service.OrderService;
import com.example.demo.util.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "toss API Controller",description = "toss 결제 요청 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/toss")
@Controller
public class TossController {


    private final OrderService orderService;
    private final TossPaymentInfra tossPaymentService;
    private final Rq rq;


    @Value("${toss.secretKey}")
    public String secretKey;

    @Operation(summary = "성공 요청",description = "검증 성공시 toss로 성공 결제 요청을 합니다.")
    @Parameter(name = "orderRequest",description = "토스 요청 dto입니다.")
    @GetMapping("/success")
    public String orderByToss(OrderRequest orderRequest) throws Exception {

        orderService.verifyRequest(orderRequest, rq.getMemberId());

        ResponseEntity<String> response = tossPaymentService.requestPermitToss(orderRequest);

        if (response.getStatusCode().is2xxSuccessful()) {
            orderService.orderPayComplete(orderRequest);
            return rq.redirectWithMsg("/order/result", "결제가 완료되었습니다.");
        }

        return rq.redirectWithMsg("/order/orderPage","결제에 실패하였습니다.");
    }

    @Operation(summary = "toss 실패",description = "프론트 toss 요청 실패")
    @Parameters({
            @Parameter(name = "message",description = "결제 실패 문구를 반환합니다."),
            @Parameter(name = "code",description = "상태 코드를 반환합니다.")
    })
    @GetMapping(value = "/fail")
    public String paymentResult(
            Model model,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code) throws Exception {

        model.addAttribute("code", code);
        model.addAttribute("message", message);

        return rq.redirectWithMsg("/order/orderPage", "결제 실패");
    }
}
