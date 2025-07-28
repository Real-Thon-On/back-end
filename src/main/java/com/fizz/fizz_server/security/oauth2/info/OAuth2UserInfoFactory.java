package com.fizz.fizz_server.security.oauth2.info;

import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import com.fizz.fizz_server.security.common.enums.ProviderInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(ProviderInfo providerInfo, Map<String, Object> attributes) {
        switch (providerInfo) {
            case GITHUB -> {
                return new GithubOAuth2UserInfo(attributes);
            }
            case KAKAO -> {
                return new KakaoOAuth2UserInfo(attributes);
            }
            case NAVER -> {
                return new NaverOAuth2UserInfo(attributes);
            }
            case GOOGLE -> {
                return new GoogleOAuth2UserInfo(attributes);
            }
        }
        throw new BusinessException(ExceptionType.INVALID_PROVIDER_TYPE_ERROR);

    }
}
