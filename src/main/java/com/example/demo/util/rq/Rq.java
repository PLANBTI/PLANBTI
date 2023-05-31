package com.example.demo.util.rq;

import com.example.demo.base.security.CustomOAuth2User;
import com.example.demo.boundedContext.member.dto.MemberDto;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Component
@RequestScope
public class Rq {

    private final CustomOAuth2User customOAuth2User;
    private final MemberDto member;
    private final MemberRepository memberRepository;

    public Rq(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;

        customOAuth2User = (CustomOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = customOAuth2User.getUsername();
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        if (optionalMember.isEmpty()) {
            member = null;
        } else {
            Member findMember = optionalMember.get();
            member = new MemberDto(findMember.getId(),  findMember.getUsername(), findMember.getEmail());
        }
    }

    public boolean isLogin() {
        return member != null;
    }
    public Long getMemberId() {
        return member.getId();
    }

    public String getUsername() {
        return member.getUsername();
    }
}
