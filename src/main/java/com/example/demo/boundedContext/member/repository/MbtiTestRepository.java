package com.example.demo.boundedContext.member.repository;

import com.example.demo.boundedContext.member.entity.MbtiTest;
import com.example.demo.boundedContext.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MbtiTestRepository extends JpaRepository<MbtiTest, Long> {
    Optional<MbtiTest> findByIdAndDeleteDateIsNull(Long id);
    Optional<MbtiTest> findByMemberUsernameAndResultAndTitleAndContent(String memberUsername, String result, String title, String content);

    List<MbtiTest> findAllByMemberUsername(String memberUsername);

}
