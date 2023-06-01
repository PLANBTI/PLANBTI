package com.example.demo.boundedContext.member.repository;

import com.example.demo.boundedContext.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> ,CustomMemberRepository{
    Optional<Member> findByUsername(String username);
    Optional<Member> findByIdAndDeleteDateIsNull(Long id);
    Optional<Member> findByUsernameAndDeleteDateIsNull(String username);
}
