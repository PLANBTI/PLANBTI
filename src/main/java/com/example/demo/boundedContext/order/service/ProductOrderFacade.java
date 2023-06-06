package com.example.demo.boundedContext.order.service;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.product.dto.ProductOrderDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductOrderFacade {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderService orderService;
    public void createOrderOne(ProductOrderDto dto, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow();
        Product product = productRepository.findById(dto.getId()).orElseThrow();

        orderService.orderProduct(member, product, product.getName(), dto.getCount());
    }
}
