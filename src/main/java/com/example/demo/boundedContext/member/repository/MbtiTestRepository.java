package com.example.demo.boundedContext.member.repository;

import com.example.demo.boundedContext.member.entity.MbtiTest;
import com.example.demo.boundedContext.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MbtiTestRepository extends JpaRepository<MbtiTest, Long> {
    Optional<MbtiTest> findByIdAndDeleteDateIsNull(Long id);
    Optional<MbtiTest> findByMemberUsernameAndResultAndTitle(String memberUsername, String result, String title);
    List<MbtiTest> findAllByMemberUsername(String memberUsername);

}
