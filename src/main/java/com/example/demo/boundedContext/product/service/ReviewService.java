package com.example.demo.boundedContext.product.service;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.product.entity.Product;

import com.example.demo.boundedContext.product.dto.ReviewDto;
import com.example.demo.boundedContext.product.entity.Review;
import com.example.demo.boundedContext.product.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new DataNotFoundException("review not found"));
    }

    public Review findByIdAndDeleteDateIsNull(Long id) {
        return reviewRepository.findByIdAndDeleteDateIsNull(id).orElseThrow(() -> new DataNotFoundException("review not found"));
    }

    public List<Review> findByMember(Member member) {
        return reviewRepository.findByMember(member);
    }

    public List<Review> findByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review create(Member member, Product product, String title, String content, String image, int rate) {
        Review review = Review.builder()
                .member(member)
                .product(product)
                .title(title)
                .content(content)
                .image(image)
                .rate(rate).build();
        reviewRepository.save(review);
        return review;
    }

    public Review modify(Review review, String title, String content, String image, int rate) {
        Review modifiedReview = review.toBuilder()
                .title(title)
                .content(content)
                .image(image)
                .rate(rate).build();
        reviewRepository.save(modifiedReview);
        return modifiedReview;
    }

    // soft-delete
    public void delete(Review review) {
        Review deletedReview = review.toBuilder()
                .deleteDate(LocalDateTime.now()).build();
        reviewRepository.save(deletedReview);
    }

    public void deleteHard(Review review) {
        reviewRepository.delete(review);
    }

    public List<ReviewDto> findByProductId(Long productId,Long offset) {
        return reviewRepository.findReviewByProductId(productId,offset);

    }
}
