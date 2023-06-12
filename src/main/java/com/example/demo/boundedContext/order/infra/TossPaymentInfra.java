package com.example.demo.boundedContext.order.infra;

import com.example.demo.base.AppEnv;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class TossPaymentInfra {

    private final RestTemplate restTemplate;

    @Value("${toss.secretKey}")
    public String secretKey;


    public ResponseEntity<String> requestPermitToss(OrderRequest orderRequest) {

        String authorizations = "Basic " + new String(Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8)));

        URI uri = UriComponentsBuilder.fromUriString(AppEnv.tossPayRequestUrl)
                .encode().build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authorizations);

        RequestEntity<OrderRequest> request = new RequestEntity<>(orderRequest, headers, HttpMethod.POST, uri);

        return restTemplate.exchange(request, String.class);
    }

    public ResponseEntity<String> refundRequest(String paymentKEy,String reason) {

        String authorizations = "Basic " + new String(Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8)));

        URI uri = UriComponentsBuilder.fromUriString(AppEnv.tossRefundRequestUrl)
                .path(paymentKEy)
                .path("cancel")
                .encode().build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authorizations);


        RequestEntity<RefundReason> request = new RequestEntity<>(new RefundReason(reason), headers, HttpMethod.POST, uri);

        return restTemplate.exchange(request, String.class);
    }

    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Data
    static class RefundReason {
        private String cancelReason;
    }


}
