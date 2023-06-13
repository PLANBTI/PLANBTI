package com.example.demo.boundedContext.product.controller;

import com.example.demo.boundedContext.product.service.ShoppingBasketService;
import com.example.demo.util.rq.ResponseData;
import com.example.demo.util.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Tag(name = "장바구니",description = "상품을 장바구니에 추가, 삭제 및 수정하는 기능")
@RequestMapping("/shopping")
@RequiredArgsConstructor
@Controller
public class ShoppingBasketController {

    private final ShoppingBasketService shoppingBasketService;
    private final Rq rq;

    @Operation(summary = "장바구니 추가",description = "선택한 물품을 장바구니에 추가합니다.")
    @Parameters({
            @Parameter(name = "productId",description = "상품 아이디"),
            @Parameter(name = "count",description = "요청 물품 개수")})

    @ResponseBody
    @GetMapping("/add")
    public ResponseData<String> addProduct(Long productId,@RequestParam(defaultValue = "1") int count) {

        return shoppingBasketService.addProduct(rq.getMemberId(), productId,count);
    }

    @Operation(summary = "장바구니 삭제",description = "선택한 물품을 장바구니에서 삭제합니다.")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Void> deleteBasket(@PathVariable Long productId) {

        return shoppingBasketService.delete(productId, rq.getMemberId());
    }
}
