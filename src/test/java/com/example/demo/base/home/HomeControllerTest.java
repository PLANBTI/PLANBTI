package com.example.demo.base.home;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class HomeControllerTest {

    @Autowired
    MockMvc mvc;

    @DisplayName("home으로 이동시 test 화면으로")
    @Test
    void home() throws Exception {
        mvc.perform(get("/"))
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/test"));

    }

}