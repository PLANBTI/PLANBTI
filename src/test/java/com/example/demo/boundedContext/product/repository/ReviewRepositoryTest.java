package com.example.demo.boundedContext.product.repository;

import com.example.demo.boundedContext.product.dto.ReviewDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    void t1() {
        Product product = Product.builder().build();
        productRepository.save(product);

        int expected = 3;

        for (int i = 0; i < expected; i++) {
            Review review = Review.builder().build();
            product.addReview(review);
            reviewRepository.save(review);
        }

        List<ReviewDto> list = reviewRepository.findReviewByProductId(product.getId(),0L);
        Assertions.assertThat(list).size().isEqualTo(expected);
    }
}