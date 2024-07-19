package com.fizz.fizz_server.global.oauth2.user;

import com.fizz.fizz_server.global.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.fizz.fizz_server.global.oauth2.user.google.GoogleOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFatory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, String accessToken, Map<String, Object> attributes) {
        if (OAuth2Provider.GOOGLE.getRegistrationId().equals(registrationId)) {
            return new GoogleOAuth2UserInfo(accessToken, attributes);
        }
        // TODO. 다른 것들 추가
        else {
            throw new OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported");
        }
    }
}
