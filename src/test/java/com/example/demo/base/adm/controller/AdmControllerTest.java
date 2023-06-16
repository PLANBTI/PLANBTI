package com.example.demo.base.adm.controller;

import com.example.demo.base.adm.service.AdmOrderDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@WithUserDetails("admin")
public class AdmControllerTest {

    @Autowired
    private AdmOrderDetailService admOrderDetailService;
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("updateToPlaced()")
    void t001() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/adm/placed/1"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdmController.class))
                .andExpect(handler().methodName("updateToPlaced"))
                .andExpect(status().is3xxRedirection());

        assertThat(admOrderDetailService.findById(1L).getStatus()).isEqualTo(PLACED);
    }

    @Test
    @DisplayName("startDelivery()")
    void t002() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/adm/startDelivery/1")
                        .with(csrf())
                        .param("invoiceNumber", "0123456789"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdmController.class))
                .andExpect(handler().methodName("startDelivery"))
                .andExpect(status().is3xxRedirection());

        assertThat(admOrderDetailService.findById(1L).getInvoiceNumber()).isNotNull();
    }

    @Test
    @DisplayName("approveExchange()")
    void t003() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/adm/approve/3"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdmController.class))
                .andExpect(handler().methodName("approveExchange"))
                .andExpect(status().is3xxRedirection());

        assertThat(admOrderDetailService.findById(3L).getStatus()).isEqualTo(APPROVED);
    }

    @Test
    @DisplayName("isDone()")
    void t004() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/adm/done/4"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdmController.class))
                .andExpect(handler().methodName("isDone"))
                .andExpect(status().is3xxRedirection());

        assertThat(admOrderDetailService.findById(4L).getStatus()).isEqualTo(DONE);
    }

}
