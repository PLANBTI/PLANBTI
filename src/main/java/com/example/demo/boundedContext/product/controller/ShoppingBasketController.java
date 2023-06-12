package com.example.demo.boundedContext.product.controller;

import com.example.demo.boundedContext.product.service.ShoppingBasketService;
import com.example.demo.util.rq.ResponseData;
import com.example.demo.util.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/shopping")
@RequiredArgsConstructor
@Controller
public class ShoppingBasketController {

    private final ShoppingBasketService shoppingBasketService;
    private final Rq rq;

    @ResponseBody
    @GetMapping("/add")
    public ResponseData<String> addProduct(Long productId,@RequestParam(defaultValue = "1") int count) {

        return shoppingBasketService.addProduct(rq.getMemberId(), productId,count);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Void> deleteBasket(@PathVariable Long productId) {

        return shoppingBasketService.delete(productId, rq.getMemberId());
    }
}
