package com.example.demo.boundedContext.order.controller;

import com.example.demo.base.AppEnv;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
@Controller
public class TossController {

    private final RestTemplate restTemplate;
    private final OrderService orderService;

    @Value("${toss.secretKey}")
    public String secretKey;

    @ResponseBody
    @GetMapping("/success")
    public ResponseEntity<String> orderByToss(OrderRequest orderRequest) throws Exception {

        if (!orderService.verifyRequest(orderRequest)) {
            return null;
        }

        return requestPermitToss(orderRequest);
    }

    @GetMapping(value = "fail")
    public String paymentResult(
            Model model,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code
    ) throws Exception {

        model.addAttribute("code", code);
        model.addAttribute("message", message);

        return "fail";
    }

    private ResponseEntity<String> requestPermitToss(OrderRequest orderRequest) {

        String authorizations = "Basic " + new String(Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8)));

        URI uri = UriComponentsBuilder.fromUriString(AppEnv.tossPayRequestUrl)
                .encode().build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authorizations);

        RequestEntity<OrderRequest> request = new RequestEntity<>(orderRequest, headers, HttpMethod.POST, uri);

        return restTemplate.exchange(request, String.class);
    }
}
