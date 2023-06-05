package com.example.demo.boundedContext.product.repository;

import com.example.demo.boundedContext.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}