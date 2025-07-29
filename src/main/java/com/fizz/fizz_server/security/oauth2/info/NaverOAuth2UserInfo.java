package com.fizz.fizz_server.security.oauth2.info;



import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getUserNameAttribute() {
        return (String) attributes.get(ProviderInfo.NAVER.getUserNameAttribute());
    }

    @Override
    public String getUserIdentifier() {
        return (String) attributes.get(ProviderInfo.NAVER.getIdentifier());
    }
}
