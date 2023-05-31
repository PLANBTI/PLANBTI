package com.example.demo.boundedContext.product.repository;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);

}