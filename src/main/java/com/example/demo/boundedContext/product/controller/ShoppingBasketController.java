package com.example.demo.boundedContext.product.controller;

import com.example.demo.boundedContext.product.service.ShoppingBasketService;
import com.example.demo.util.rq.ResponseData;
import com.example.demo.util.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/shopping")
@RequiredArgsConstructor
@Controller
public class ShoppingBasketController {

    private final ShoppingBasketService shoppingBasketService;
    private final Rq rq;

    @ResponseBody
    @GetMapping("/add")
    public String addProduct(Long productId) {

        ResponseData<String> responseData = shoppingBasketService.addProduct(rq.getMemberId(), productId);

        return responseData.isSuccess() ? responseData.getMsg() : "장바구니에 담는데 실패하였습니다.";
    }
}
