package com.example.demo.boundedContext.product.repository;

import com.example.demo.boundedContext.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryDslRepository {
    Page<Product> findAllByCategoryName(String categoryName, String keyword, Pageable pageable);
}
