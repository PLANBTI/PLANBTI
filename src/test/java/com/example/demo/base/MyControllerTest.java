package com.example.demo.base;

import com.example.demo.base.home.HomeController;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class MyControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .username("user2")
                .email("user2@naver.com")
                .password(passwordEncoder.encode("1111"))
                .build();
        member.addRole(Role.USER);
        Member save = memberRepository.save(member);

    }

    @WithUserDetails(value = "user2", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("USER권한 접속 테스트")
    @Test
    void t1() throws Exception {
        mvc.perform(get("/member"))
                .andExpect(handler().methodName("member"))
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(status().is2xxSuccessful());
    }

}