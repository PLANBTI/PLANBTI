package com.example.demo.base;

import com.example.demo.aggreagate.member.entity.Member;
import com.example.demo.aggreagate.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .username("yohan")
                .email("sjohn@naver.com")
                .password(passwordEncoder.encode("1111"))
                .build();
        member.addRole(Role.USER);
        Member save = memberRepository.save(member);
    }

    @WithUserDetails(value = "yohan",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("USER권한 접속 테스트")
    @Test
    void t1() throws Exception {
        mvc.perform(get("/member"))
                .andExpect(MockMvcResultMatchers.handler().methodName("member"))
                .andExpect(MockMvcResultMatchers.handler().handlerType(MyController.class))
                .andExpect(status().is2xxSuccessful());
    }

}