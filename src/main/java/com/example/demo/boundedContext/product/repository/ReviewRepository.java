package com.example.demo.boundedContext.product.repository;

<<<<<<< HEAD
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>,CustomReviewRepository {
    List<Review> findByMember(Member member);
    List<Review> findByProduct(Product product);
    Optional<Review> findByIdAndDeleteDateIsNull(Long id);
}
