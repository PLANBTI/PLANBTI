package com.example.demo.boundedContext.member.repository;

import com.example.demo.boundedContext.member.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {
    Optional<Test> findByIdAndDeleteDateIsNull(Long id);
}
