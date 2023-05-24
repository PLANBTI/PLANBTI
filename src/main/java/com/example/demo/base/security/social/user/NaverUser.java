package com.example.demo.base.security.social.user;

import com.example.demo.base.security.social.OAuth2Provider;
import com.example.demo.base.security.social.inter.DivideOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class NaverUser extends DivideOAuth2User {

    public NaverUser(OAuth2User oAuth2User) {
        super(oAuth2User, oAuth2User.getAttribute("response"));

    }

    @Override
    public String getUsername() {
        return getProviderCode() + "__" + getOAuth2Name();
    }

    @Override
    public String getOAuth2Name() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getProviderCode() {
        return OAuth2Provider.NAVER.name();
    }

}
