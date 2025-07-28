package com.fizz.fizz_server.security.oauth2.info;



import com.fizz.fizz_server.security.common.enums.ProviderInfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get(ProviderInfo.NAVER.getAttributeKey()));
    }

    @Override
    public String getProviderCode() {
        return (String) attributes.get(ProviderInfo.NAVER.getProviderCode());
    }

    @Override
    public String getUserIdentifier() {
        return (String) attributes.get(ProviderInfo.NAVER.getIdentifier());
    }
}
