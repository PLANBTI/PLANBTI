package com.example.demo.boundedContext.member.repository;

import com.example.demo.boundedContext.member.entity.MbtiTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MbtiTestRepository extends JpaRepository<MbtiTest, Long> {
    Optional<MbtiTest> findByIdAndDeleteDateIsNull(Long id);
}
