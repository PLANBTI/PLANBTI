package com.example.demo.boundedContext.order.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class OrderControllerTest {

    @Autowired
    MockMvc mvc;

    @WithUserDetails(value = "user1",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("orderPage get요청")
    @Test
    void t1() throws Exception {
        mvc.perform(get("/order/orderPage"))
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("orderPage"))
                .andExpect(status().is3xxRedirection());
    }
}