package com.example.demo.boundedContext.order.controller;

import com.example.demo.util.rq.Rq;
import com.example.demo.base.AppEnv;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
@RequestMapping("/toss")
@Controller
public class TossController {

    private final RestTemplate restTemplate;
    private final OrderService orderService;
    private final Rq rq;

    @Value("${toss.secretKey}")
    public String secretKey;

    @GetMapping("/success")
    public String orderByToss(Model model,OrderRequest orderRequest) throws Exception {

        if (!orderService.verifyRequest(orderRequest,rq.getMemberId())) {
            return "order/orderPage";
        }

        ResponseEntity<String> response = requestPermitToss(orderRequest);
        if (response.getStatusCode().is2xxSuccessful()) {
            orderService.orderPayComplete(orderRequest);
            model.addAttribute("result","success");
        } else {
            model.addAttribute("result","fail");
        }

        return "redirect:/order/result";
    }

    @GetMapping(value = "fail")
    public String paymentResult(
            Model model,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code
    ) throws Exception {

        model.addAttribute("code", code);
        model.addAttribute("message", message);

        return "redirect:/order/orderPage";
    }

    private  ResponseEntity<String> requestPermitToss(OrderRequest orderRequest) {

        String authorizations = "Basic " + new String(Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8)));

        URI uri = UriComponentsBuilder.fromUriString(AppEnv.tossPayRequestUrl)
                .encode().build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authorizations);

        RequestEntity<OrderRequest> request = new RequestEntity<>(orderRequest, headers, HttpMethod.POST, uri);

        ResponseEntity<String> exchange = restTemplate.exchange(request, String.class);

        return exchange;
    }
}
