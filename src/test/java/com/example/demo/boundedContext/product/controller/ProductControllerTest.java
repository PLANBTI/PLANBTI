package com.example.demo.boundedContext.product.controller;

import com.example.demo.boundedContext.order.service.ProductFacade;
import com.example.demo.boundedContext.product.dto.ProductOrderDto;
import com.example.demo.boundedContext.product.dto.ReviewDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.service.ProductService;
import com.example.demo.boundedContext.product.service.ReviewService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
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
class ProductControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ProductService productService;

    @MockBean
    ReviewService reviewService;

    @MockBean
    ProductFacade facade;

    @WithUserDetails(value = "user1",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("product get요청")
    @Test
    void t1() throws Exception {

        Mockito.when(productService.findById(1L)).thenReturn(Product.builder().name("product").count(100).build());

        mvc.perform(get("/product/detail/%d".formatted(1L)))
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("viewDetailProduct"))
                .andExpect(content().string(containsString("""
                        <input name="count" id="countInput"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                         <button class="btn btn-outline btn-primary mt-4" onclick="requestBasket()"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                         <button id="reviewButton" class="btn btn
                        """.stripIndent().trim())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/detail"))
                .andDo(print());
    }

    @WithUserDetails(value = "user1",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("product 단일 구매 요청 성공")
    @Test
    void t2() throws Exception {

        Long productId  = 20L;
        Long memberId = 1L;
        int count = 30;

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id",String.valueOf(productId));
        map.add("count",String.valueOf(count));

        ProductOrderDto dto = new ProductOrderDto();
        dto.setId(productId);
        dto.setCount(count);

        Mockito.when(productService.findById(productId)).thenReturn(Product.builder().count(100).build());
        Mockito.doNothing().when(facade).createOrderOne(dto,memberId);

        mvc.perform(post("/product/order")
                        .with(csrf()).params(map))
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("orderProduct"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/order/orderPage**"))
                .andDo(print());
    }

    @WithUserDetails(value = "user1",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("product 단일 구매 요청 실패")
    @Test
    void t3() throws Exception {

        Long productId  = 20L;
        int count = 0;

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id",String.valueOf(productId));
        map.add("count",String.valueOf(count));

        mvc.perform(post("/product/order")
                        .with(csrf()).params(map))
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("orderProduct"))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @WithUserDetails(value = "user1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("product more review")
    @Test
    void t4() throws Exception {


        ArrayList<ReviewDto> list = new ArrayList<>();
        list.add(new ReviewDto("username","title","content","image",10, LocalDateTime.now()));

        long productId = 20L;
        Mockito.when(reviewService.findByProductId(productId,11L)).thenReturn(list);
        mvc.perform(post("/product/more/"+ productId)
                        .with(csrf()).param("offset","11"))
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("moreReview"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].username").value(equalTo("username")))
                .andExpect(jsonPath("$[0].rate").value(equalTo(10)))
                .andDo(print());
    }


}