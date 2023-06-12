package com.example.demo.boundedContext.product.repository.review;

import com.example.demo.boundedContext.product.dto.ReviewDto;

import java.util.List;

public interface CustomReviewRepository {

    List<ReviewDto> findReviewByProductId(Long productId,Long offset);
}
