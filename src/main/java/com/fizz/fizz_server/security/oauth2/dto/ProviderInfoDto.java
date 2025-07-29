package com.fizz.fizz_server.security.oauth2.dto;


import com.fizz.fizz_server.security.oauth2.enums.ProviderType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProviderInfoDto {

    private final ProviderType providerType; // provider 벤더명
    private final String userNameAttribute;  // 해당 provider 상의 식별자 키명
    private final String identifier;  // 해당 provider 상의 식별자 값


}
