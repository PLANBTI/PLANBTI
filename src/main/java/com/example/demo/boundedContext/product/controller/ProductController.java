package com.example.demo.boundedContext.product.controller;

import com.example.demo.boundedContext.order.service.ProductOrderFacade;
import com.example.demo.boundedContext.product.dto.ProductOrderDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.service.ProductService;
import com.example.demo.util.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/product")
@Controller
public class ProductController {

    private final Rq rq;
    private final ProductService productService;
    private final ProductOrderFacade productOrderService;

    @GetMapping("/detail/{id}")
    public String viewDetailProduct(Model model, @PathVariable(name = "id") Long productId) {

        Product product = productService.findById(productId);

        model.addAttribute("product", product);

        return "product/detail";
    }

    @PostMapping("/order")
    public String orderProduct(@Valid ProductOrderDto dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return rq.historyBack("잘못된 요청입니다.");


        Product product = productService.findById(dto.getId());

        if (product.getCount() < dto.getCount())
            return rq.historyBack("잘못된 요청입니다.");

        productOrderService.createOrderOne(dto, rq.getMemberId());

        return rq.redirectWithMsg("/order/orderPage","주문하러 가기");
    }


}
