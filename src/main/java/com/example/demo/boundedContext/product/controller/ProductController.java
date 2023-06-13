package com.example.demo.boundedContext.product.controller;

import com.example.demo.boundedContext.order.service.ProductFacade;
import com.example.demo.boundedContext.product.dto.ProductOrderDto;
import com.example.demo.boundedContext.product.dto.ReviewDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.service.ProductService;
import com.example.demo.boundedContext.product.service.ReviewService;
import com.example.demo.util.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "상품 주문 및 리뷰", description = "상품 주문, 리뷰, 장바구니 기능")
@RequiredArgsConstructor
@RequestMapping("/product")
@Controller
public class ProductController {

    private final Rq rq;
    private final ReviewService reviewService;
    private final ProductService productService;
    private final ProductFacade productFacade;

    @Operation(summary = "상품 상세", description = "상품에 대한 정보를 상세하게 보여줍니다.")
    @GetMapping("/detail/{id}")
    public String viewDetailProduct(Model model, @PathVariable(name = "id") Long productId) {

        Product product = productService.findById(productId);
        List<ReviewDto> reviewList = reviewService.findByProductId(productId, 0L);
        model.addAttribute("product", product);
        model.addAttribute("reviewList", reviewList);

        return "product/detail";
    }

    @Operation(summary = "리뷰 요청", description = "해당 물품의 리뷰를 추가로 보여줍니다.")
    @Parameters({
            @Parameter(name = "productId", description = "물품 고유 아이디"),
            @Parameter(name = "offset", description = "추가 리뷰 개수")})
    @ResponseBody
    @PostMapping("/more/{id}")
    public List<ReviewDto> moreReview(@PathVariable(name = "id") Long productId, Long offset) {

        if (offset > 50L)
            return null;

        return reviewService.findByProductId(productId, offset);
    }

    @Operation(summary = "주문 요청", description = "주문 요청을 검증하고 결과값을 반환합니다")
    @Parameter(name = "dto", description = "물품 고유 아이디와 개수")
    @PostMapping("/order")
    public String orderProduct(@Valid ProductOrderDto dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return rq.historyBack("잘못된 요청입니다.");


        Product product = productService.findById(dto.getId());

        if (product.getCount() < dto.getCount())
            return rq.historyBack("잘못된 요청입니다.");

        productFacade.createOrderOne(dto, rq.getMemberId());

        return rq.redirectWithMsg("/order/orderPage", "주문하러 가기");
    }

    @Operation(summary = "장바구니 주문 요청", description = "장바구니 주문 요청을 검증하고 결과값을 반환합니다")
    @Parameter(name = "productIdList", description = "물품 고유 아이디들의 집합")
    @PostMapping("/basketOrder")
    public String basketOrder(@RequestParam(name = "productId") List<Long> productIdList) {

        productFacade.createBulkOrder(productIdList, rq.getMemberId());

        return rq.redirectWithMsg("/order/orderPage", "주문하러 가기");
    }


}
