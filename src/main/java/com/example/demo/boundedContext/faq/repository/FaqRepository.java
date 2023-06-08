package com.example.demo.boundedContext.faq.repository;

import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    Optional<Faq> findByIdAndDeleteDateIsNull(Long id);
    List<Faq> findByMemberAndDeleteDateIsNull(Member member);
}
