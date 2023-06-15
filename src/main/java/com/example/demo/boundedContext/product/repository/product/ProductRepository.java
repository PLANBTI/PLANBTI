package com.example.demo.boundedContext.product.repository.product;

import com.example.demo.boundedContext.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueryDslRepository {
    Optional<Product> findByName(String name);

    Optional<Product> findByIdAndDeleteDateIsNull(Long id);

    Optional<Product> findById(Long id);

    //Page<Product> findAllByDeleteDateIsNull(Pageable pageable);
}