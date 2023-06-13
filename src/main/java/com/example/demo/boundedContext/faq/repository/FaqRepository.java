package com.example.demo.boundedContext.faq.repository;

import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    Optional<Faq> findByIdAndDeleteDateIsNull(Long id);

    @Query("SELECT f FROM Faq f WHERE f.member =:member AND f.deleteDate IS NULL")
    List<Faq> findByMember(@Param("member") Member member);
}
