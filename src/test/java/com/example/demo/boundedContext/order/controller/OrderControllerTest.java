package com.example.demo.boundedContext.order.controller;

import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.dto.OrderExchangeDto;
import com.example.demo.boundedContext.order.repository.OrderDetailRepository;
import com.example.demo.boundedContext.order.service.OrderDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class OrderControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    OrderDetailRepository orderDetailRepository;
    @MockBean
    OrderDetailService orderDetailService;

    @Autowired
    MemberRepository memberRepository;

    @WithUserDetails(value = "user1",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("orderPage get요청")
    @Test
    void t1() throws Exception {
        mvc.perform(get("/order/orderPage"))
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("orderPage"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("order/orderPage"))
                .andDo(print());
    }

    @WithUserDetails(value = "user1",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("orderResult get요청")
    @Test
    void t2() throws Exception {
        mvc.perform(get("/order/result"))
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("resultPage"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("order/result"))
                .andDo(print());
    }

    @WithUserDetails(value = "user1",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("exchange get요청")
    @Test
    void t4() throws Exception {

        long orderId = 20L;
        long orderItemId = 20L;
        long memberId =1L;
        int count = 10;
        int price = 10000;
        String productName = "product";

        OrderExchangeDto dto = new OrderExchangeDto(orderItemId, productName, count, price);

        Mockito.when(orderDetailService.findOrderDetailById(orderId, orderItemId, memberId))
                        .thenReturn(dto);
        mvc.perform(get("/order/exchange/%d?orderItemId=%d".formatted(orderId,orderItemId)))
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("exchangeGet"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("order/exchange"))
                .andDo(print());

    }

    @WithUserDetails(value = "user1",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("orderResult post요청")
    @Test
    void t5() throws Exception {

        long orderId = 20L;
        long orderItemId = 20L;
        int count = 10;
        int price = 10000;
        String productName = "product";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("orderItemId", String.valueOf(orderItemId));
        map.add("productName", "product");
        map.add("count", String.valueOf(count));
        map.add("price", String.valueOf(price));
        map.add("exchange", "RETURN");
        map.add("reason", "reason");

        OrderExchangeDto dto = new OrderExchangeDto(orderItemId, productName, count, price);

        Mockito.doNothing().when(orderDetailService).returnProduct(orderId,dto,1L);

        mvc.perform(post("/order/exchange/%d".formatted(orderId))
                        .with(csrf())
                        .params(map))
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("exchangePost"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/order/orderInfo**"))
                .andDo(print());
    }
}