package com.example.demo.boundedContext.product.repository.product;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueryDslRepository {
    Optional<Product> findByName(String name);

    Optional<Product> findByIdAndDeleteDateIsNull(Long id);


}