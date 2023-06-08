package com.example.demo.boundedContext.product.service;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("존재하지 않는 Product입니다."));
    }

    public Product findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 데이터입니다"));
    }

    public Page<Product> findAllByCategoryNameAndKeyword(String categoryName, String keyword, Pageable pageable) {
        return productRepository.findAllByCategoryNameAndKeyword(categoryName, keyword, pageable);
    }

}
