package com.example.demo.boundedContext.product.repository;

import com.example.demo.boundedContext.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueryDslRepository {
    Optional<Product> findByName(String name);

    Optional<Product> findByIdAndDeleteDateIsNull(Long id);

    Optional<Product> findById(Long id);

}