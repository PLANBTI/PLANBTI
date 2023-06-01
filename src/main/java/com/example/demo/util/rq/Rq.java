package com.example.demo.util.rq;

import com.example.demo.base.security.CustomOAuth2User;
import com.example.demo.boundedContext.member.dto.MemberDto;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class Rq {

    private CustomOAuth2User customOAuth2User;
    private MemberDto member;
    private final MemberRepository memberRepository;

    public Rq(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            this.customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            String username = customOAuth2User.getUsername();
            Member findMember = memberRepository.findByUsername(username).orElseThrow();
            member = new MemberDto(findMember.getId(), findMember.getUsername(), findMember.getEmail());
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
