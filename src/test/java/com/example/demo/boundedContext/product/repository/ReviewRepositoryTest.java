package com.example.demo.boundedContext.product.repository;

import com.example.demo.boundedContext.product.dto.ReviewDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.Review;
import com.example.demo.boundedContext.product.repository.product.ProductRepository;
import com.example.demo.boundedContext.product.repository.review.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("review 가져오기 test")
    void t1() {
        Product product = Product.builder().build();
        productRepository.save(product);

        int expected = 15;

        for (int i = 0; i < expected; i++) {
            Review review = Review.builder().title("title"+i).build();
            product.addReview(review);
            reviewRepository.save(review);
        }

        List<ReviewDto> list = reviewRepository.findReviewByProductId(product.getId(),11L);
        assertThat(list.get(0).getTitle()).isEqualTo("title11");
        assertThat(list).size().isEqualTo(4);
    }
}