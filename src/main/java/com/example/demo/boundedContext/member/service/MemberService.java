package com.example.demo.boundedContext.member.service;

import com.example.demo.base.Role;
import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.base.security.social.inter.DivideOAuth2User;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new DataNotFoundException("존재하지 않는 유저입니다."));
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("존재하지 않는 유저입니다."));
    }

    public Member findByIdAndDeleteDateIsNull(Long id) {
        return memberRepository.findByIdAndDeleteDateIsNull(id).orElseThrow(() -> new DataNotFoundException("존재하지 않는 유저입니다."));
    }

    public Member findByUsernameAndDeleteDateIsNull(String username) {
        return memberRepository.findByUsernameAndDeleteDateIsNull(username).orElseThrow(() -> new DataNotFoundException("존재하지 않는 유저입니다."));
    }

    @Transactional
    public Member modify(Member member, String email, String phoneNumber) {
        Member modifiedMember = member.toBuilder()
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        memberRepository.save(modifiedMember);
        return modifiedMember;
    }

    // soft-delete
    @Transactional
    public void delete(Member member) {
        Member deletedMember = member.toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();

        memberRepository.save(deletedMember);
    }

    @Transactional
    public void whenAfterCreateAddress(Member member, Address address) {
        List<Address> list = member.getAddresses();
        list.add(address);

        Member member1 = member.toBuilder()
                .addresses(list)
                .build();
        memberRepository.save(member1);
    }

    @Transactional
    public void whenAfterModifyAddress(Member member, Address address, Address modifiedAddress) {
        List<Address> list = member.getAddresses();
        list.remove(address);
        list.add(modifiedAddress);

        Member member1 = member.toBuilder()
                .addresses(list)
                .build();
        memberRepository.save(member1);
    }
}
