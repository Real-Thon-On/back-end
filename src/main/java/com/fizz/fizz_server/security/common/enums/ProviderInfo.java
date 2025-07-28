package com.fizz.fizz_server.security.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ProviderInfo {
    GOOGLE("google", "sub", "email"),
    NAVER("naver", "id", "email"),
    KAKAO("kakao", "id", "email");

    private final String registrationId;     // application.yml의 registrationId
    private final String userNameAttribute;  // provider가 사용하는 식별자 키명
    private final String identifier;

    public static ProviderInfo from(String provider) {
        String upperCastedProvider = provider.toUpperCase();

        return Arrays.stream(ProviderInfo.values())
                .filter(item -> item.name().equals(upperCastedProvider))
                .findFirst()
                .orElseThrow();
    }
}
