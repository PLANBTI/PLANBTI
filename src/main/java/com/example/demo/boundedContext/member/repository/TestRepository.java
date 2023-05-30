package com.example.demo.boundedContext.member.repository;

import com.example.demo.boundedContext.member.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
