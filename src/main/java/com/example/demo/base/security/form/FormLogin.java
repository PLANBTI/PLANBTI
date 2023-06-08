package com.example.demo.base.security.form;

import com.example.demo.base.security.CustomOAuth2User;
import com.example.demo.boundedContext.member.dto.MemberDto;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class FormLogin implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저 없음"));

        publisher.publishEvent(new MemberDto(member.getId(), member.getUsername(),member.getEmail()));


        return new CustomOAuth2User(member.getUsername(), member.getPassword(), member.getAuthorities());
    }
}
