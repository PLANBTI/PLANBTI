package com.example.demo.boundedContext.product.controller;

import com.example.demo.boundedContext.order.service.OrderService;
import com.example.demo.boundedContext.order.service.ProductFacade;
import com.example.demo.boundedContext.product.dto.ProductOrderDto;
import com.example.demo.boundedContext.product.dto.ReviewDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.service.ProductService;
import com.example.demo.boundedContext.product.service.ReviewService;
import com.example.demo.boundedContext.product.service.ShoppingBasketService;
import com.example.demo.util.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/product")
@Controller
public class ProductController {

    private final Rq rq;
    private final ReviewService reviewService;
    private final ProductService productService;
    private final ShoppingBasketService shoppingBasketService;
    private final OrderService orderService;
    private final ProductFacade productFacade;

    @GetMapping("/detail/{id}")
    public String viewDetailProduct(Model model, @PathVariable(name = "id") Long productId) {

        Product product = productService.findById(productId);
        List<ReviewDto> reviewList = reviewService.findByProductId(productId, 0L);
        model.addAttribute("product", product);
        model.addAttribute("reviewList", reviewList);

        return "product/detail";
    }

    @ResponseBody
    @PostMapping("/more/{id}")
    public List<ReviewDto> moreReview(@PathVariable(name = "id") Long productId, Long offset) {

        if (offset > 50L)
            return null;

        return reviewService.findByProductId(productId, offset);
    }

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

    @PostMapping("/basketOrder")
    public String basketOrder(@RequestParam("selectedProducts") List<Long> selectedProducts,
                              Long shoppingId) {

        shoppingBasketService.checkOwner(shoppingId, rq.getMemberId());

        productFacade.createBulkOrder(selectedProducts, rq.getMemberId());


        return rq.redirectWithMsg("/order/orderPage", "주문하러 가기");
    }



}
