package com.example.demo.boundedContext.order.service;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.product.dto.ProductOrderDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import com.example.demo.boundedContext.product.service.ProductService;
import com.example.demo.boundedContext.product.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductFacade {

    private final ProductService productService;
    private final MemberService memberService;
    private final OrderService orderService;

    public void createOrderOne(ProductOrderDto dto, Long memberId) {

        Member member = memberService.findById(memberId);
        Product product = productService.findById(dto.getId());

        orderService.orderProduct(member, product, product.getName(), dto.getCount());
    }
}
