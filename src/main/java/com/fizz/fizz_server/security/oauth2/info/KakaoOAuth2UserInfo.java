package com.fizz.fizz_server.security.oauth2.info;



import com.fizz.fizz_server.security.common.enums.ProviderInfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    private String providerId;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get(ProviderInfo.KAKAO.getAttributeKey()));
        this.providerId = String.valueOf(attributes.get(ProviderInfo.KAKAO.getIdentifier()));
    }

    @Override
    public String getProviderCode() {
        return providerId;
    }

    @Override
    public String getUserIdentifier() {
        return (String) attributes.get(ProviderInfo.KAKAO.getProviderCode());
    }
}
