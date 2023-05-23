package com.example.demo.base.security.social;

import com.example.demo.base.exception.NotSupportUserLoginException;
import com.example.demo.base.security.social.user.GoogleUser;
import com.example.demo.base.security.social.user.KakaoUser;
import com.example.demo.base.security.social.user.NaverUser;
import com.example.demo.base.security.social.inter.DivideOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static com.example.demo.base.security.social.OAuth2Provider.*;

public class SocialUserFactory {

    public static DivideOAuth2User create(String providerTypeCode, OAuth2User oAuth2User) {

        if (isMatchWithProvider(providerTypeCode, GOOGLE)) {
            return new GoogleUser(oAuth2User);
        }

        if (isMatchWithProvider(providerTypeCode, KAKAO)) {
            return new KakaoUser(oAuth2User);
        }

        if (isMatchWithProvider(providerTypeCode, NAVER)) {
            return new NaverUser(oAuth2User);
        }

        throw new NotSupportUserLoginException("지원하지 않는 로그인 방식입니다.");
    }

    private static boolean isMatchWithProvider(String providerTypeCode, OAuth2Provider provider) {
        return providerTypeCode.equals(provider.name());
    }

}


