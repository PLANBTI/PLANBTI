package com.example.demo.base.security.social.inter;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

public interface SocialUserInterface {
    String getUsername();
    String getEmail();

    String getOAuth2Name();

    String getProviderCode();
    Collection<? extends GrantedAuthority> getAuthorities();
    String getPassword();
    Map<String, Object> getAttributes();

}
