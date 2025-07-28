package com.fizz.fizz_server.security.oauth2.info;


import com.fizz.fizz_server.security.common.enums.ProviderInfo;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderCode() {
        return (String) attributes.get(ProviderInfo.GITHUB.getProviderCode());
    }

    @Override
    public String getUserIdentifier() {
        return (String) attributes.get(ProviderInfo.GITHUB.getIdentifier());
    }
}
