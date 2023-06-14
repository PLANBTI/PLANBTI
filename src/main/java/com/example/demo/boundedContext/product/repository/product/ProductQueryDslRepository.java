package com.example.demo.boundedContext.product.repository.product;

import com.example.demo.boundedContext.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryDslRepository {
    Page<Product> findAllByCategoryNameAndKeyword(String categoryName, String keyword, Pageable pageable);

    Page<Product> findAllForPaging(Pageable pageable);
}
