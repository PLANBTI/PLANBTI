package com.example.demo.boundedContext.product.repository;

import com.example.demo.boundedContext.product.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("updateProductCount test")
    void t1() {
        Product save = productRepository.save(Product.builder().name("product").price(1000).count(5).build());
        Product product = productRepository.findByIdWithLock(save.getId()).orElseThrow();

        Assertions.assertThat(product.getPrice()).isEqualTo(1000);
        Assertions.assertThat(product.getName()).isEqualTo("product");

        product.updateProductCount(3);

        Assertions.assertThat(product.getCount()).isEqualTo(2);
    }

}