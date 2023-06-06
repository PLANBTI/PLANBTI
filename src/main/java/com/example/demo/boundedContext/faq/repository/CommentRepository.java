package com.example.demo.boundedContext.faq.repository;

import com.example.demo.boundedContext.faq.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndDeleteDateIsNull(Long id);
}
