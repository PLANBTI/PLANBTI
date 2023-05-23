package com.example.demo.base.security.form;

import com.example.demo.aggreagate.member.entity.Member;
import com.example.demo.aggreagate.member.repository.MemberRepository;
import com.example.demo.base.security.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FormLogin implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저 없음"));

        return new CustomOAuth2User(member.getUsername(), member.getPassword(), member.getAuthorities());
    }
}
