package com.example.demo.boundedContext.member.repository;

import com.example.demo.boundedContext.member.entity.Member;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository{

    @Query("SELECT m FROM Member m where m.username =:username AND m.deleteDate IS NULL")
    Optional<Member> findByUsername(@Param("username") String username);

    Optional<Member> findByIdAndDeleteDateIsNull(Long id);

    @Modifying(flushAutomatically = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Query(value = "DELETE FROM member WHERE id = ?1", nativeQuery = true)
    void deleteHardById(Long id);
}
