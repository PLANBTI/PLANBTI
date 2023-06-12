package com.example.demo.boundedContext.order.controller;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.dto.OrderRequest;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderStatus;
import com.example.demo.boundedContext.order.infra.TossPaymentInfra;
import com.example.demo.boundedContext.order.repository.order.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class TossControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @MockBean
    TossPaymentInfra tossPaymentService;

    @DisplayName("toss success test")
    @WithUserDetails("user1")
    @Test
    void t1() throws Exception {

        Member member = memberRepository.findByUsername("user1").orElseThrow();

        Order order = orderRepository.findOrderOneByStatus(member.getId(), OrderStatus.BEFORE).orElseThrow();

        OrderRequest orderRequest = new OrderRequest("paymentKey",order.getUuid(),order.getTotalPrice());

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("paymentKey",orderRequest.getPaymentKey());
        map.add("orderId",orderRequest.getOrderId());
        map.add("amount", String.valueOf(orderRequest.getAmount()));

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        Mockito.when(tossPaymentService.requestPermitToss(orderRequest)).thenReturn(responseEntity);

        mvc.perform(get("/toss/success").params(map))
                .andExpect(handler().handlerType(TossController.class))
                .andExpect(handler().methodName("orderByToss"))
                .andExpect(redirectedUrlPattern("/order/result**"));
    }

    @DisplayName("toss fail test")
    @WithUserDetails("user1")
    @Test
    void t2() throws Exception {

        Member member = memberRepository.findByUsername("user1").orElseThrow();

        Order order = orderRepository.findOrderOneByStatus(member.getId(), OrderStatus.BEFORE).orElseThrow();

        OrderRequest orderRequest = new OrderRequest("paymentKey",order.getUuid(),order.getTotalPrice());

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("paymentKey",orderRequest.getPaymentKey());
        map.add("orderId",orderRequest.getOrderId());
        map.add("amount", String.valueOf(orderRequest.getAmount()));

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Mockito.when(tossPaymentService.requestPermitToss(orderRequest)).thenReturn(responseEntity);

        mvc.perform(get("/toss/success").params(map))
                .andExpect(handler().handlerType(TossController.class))
                .andExpect(handler().methodName("orderByToss"))
                .andExpect(redirectedUrlPattern("/order/orderPage**"))
                .andDo(print());
    }


}