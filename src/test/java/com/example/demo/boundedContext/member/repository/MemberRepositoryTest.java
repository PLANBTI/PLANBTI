package com.example.demo.boundedContext.member.repository;

import com.example.demo.boundedContext.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.boundedContext.member.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    Member user;
    
    @BeforeEach
    void setUp() {
        
        String username = "user20";
        user = memberRepository.save(Member.builder()
                .username(username)
                .password("1111")
                .build());
    }

    @DisplayName("member soft delete 검증1")
    @Test
    void t1() {

        Member fetch = jpaQueryFactory.selectFrom(member)
                .where(member.username.eq(user.getUsername()).and(member.deleteDate.isNull()))
                .fetch()
                .get(0);

        assertThat(fetch.getUsername()).isEqualTo(user.getUsername());
        assertThat(fetch.getDeleteDate()).isNull();
    }

    @DisplayName("member soft delete 검증2")
    @Test
    void t2() {

        memberRepository.delete(user);

        Member fetch = jpaQueryFactory.selectFrom(member)
                .where(member.username.eq(user.getUsername()).and(member.deleteDate.isNotNull()))
                .fetch()
                .get(0);

        assertThat(fetch.getUsername()).isEqualTo(user.getUsername());
        assertThat(fetch.getDeleteDate()).isNotNull();
    }

}