package com.example.demo.base.event.listen;

import com.example.demo.base.redis.MemberDtoRepository;
import com.example.demo.base.security.CustomOAuth2User;
import com.example.demo.boundedContext.member.dto.MemberChangeDto;
import com.example.demo.boundedContext.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Component
@RequiredArgsConstructor
public class BaseEventListener {

    private final MemberDtoRepository memberDtoRepository;
    private final UserDetailsService userDetailsService;

    @EventListener
    public void loginUser(MemberDto memberDto) {
        memberDtoRepository.save(memberDto);
    }

    @EventListener
    public void changeUserInfo(MemberChangeDto dto) {

        memberDtoRepository.deleteById(dto.getId());
        memberDtoRepository.save(new MemberDto(dto.getId(), dto.getUsername(), dto.getEmail()));


        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        if (authentication1 instanceof OAuth2AuthenticationToken authentication) {
            String registrationId =
                    authentication.getAuthorizedClientRegistrationId();

            CustomOAuth2User oAuth2User = new CustomOAuth2User(dto.getUsername(),
                    (String) authentication.getCredentials(), authentication.getAuthorities());

            OAuth2AuthenticationToken token =
                    new OAuth2AuthenticationToken(oAuth2User, oAuth2User.getAuthorities(), registrationId);

            SecurityContextHolder.getContext().setAuthentication(token);

        } else {

            CustomOAuth2User newPrincipal = (CustomOAuth2User) userDetailsService.loadUserByUsername(dto.getUsername());
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(newPrincipal, newPrincipal.getPassword(), newPrincipal.getAuthorities());

            token.setDetails(SecurityContextHolder.getContext().getAuthentication().getDetails());

            SecurityContextHolder.getContext().setAuthentication(token);

        }


    }
}
