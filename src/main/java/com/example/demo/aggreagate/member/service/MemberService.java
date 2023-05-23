package com.example.demo.aggreagate.member.service;

import com.example.demo.aggreagate.member.entity.Member;
import com.example.demo.aggreagate.member.repository.MemberRepository;
import com.example.demo.base.Role;
import com.example.demo.base.security.social.inter.DivideOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member saveOAuth2Member(DivideOAuth2User customOAuth2User) {
        Optional<Member> findByName = memberRepository.findByUsername(customOAuth2User.getUsername());

        if (findByName.isEmpty()) {
            String encode = passwordEncoder.encode(customOAuth2User.getPassword());

            Member member = Member.builder()
                    .password(encode)
                    .username(customOAuth2User.getUsername())
                    .email(customOAuth2User.getEmail())
                    .build();
            member.addRole(Role.USER);

            return memberRepository.save(member);
        }

        return findByName.get();
    }

}
