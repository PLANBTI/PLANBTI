package com.example.demo.base.security.social.user;

import com.example.demo.base.security.social.OAuth2Provider;
import com.example.demo.base.security.social.inter.DivideOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class KakaoUser extends DivideOAuth2User {

    public KakaoUser(OAuth2User oAuth2User) {
        super(oAuth2User, oAuth2User.getAttribute("kakao_account"));
    }
    @Override
    public String getUsername() {
        return getProviderCode() + "__" + getOAuth2Name();
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getOAuth2Name() {
        return this.oAuth2User.getName();
    }
    @Override
    public String getProviderCode() {
        return OAuth2Provider.KAKAO.name();
    }

}