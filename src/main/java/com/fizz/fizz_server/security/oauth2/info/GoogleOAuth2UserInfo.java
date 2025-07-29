package com.fizz.fizz_server.security.oauth2.info;



import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getUserNameAttribute() {
        return (String) attributes.get(ProviderInfo.GOOGLE.getUserNameAttribute());
    }

    @Override
    public String getUserIdentifier() {
        return (String) attributes.get(ProviderInfo.GOOGLE.getIdentifier());
    }
}
