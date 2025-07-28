package com.fizz.fizz_server.security.oauth2.service;




import com.fizz.fizz_server.security.common.enums.ProviderInfo;
import com.fizz.fizz_server.security.common.enums.Role;
import com.fizz.fizz_server.security.oauth2.info.OAuth2UserInfo;
import com.fizz.fizz_server.security.oauth2.info.OAuth2UserInfoFactory;
import com.fizz.fizz_server.security.oauth2.principal.UserPrincipal;
import com.fizz.fizz_server.user.entity.User;
import com.fizz.fizz_server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // provider 가 넘겨준 유저 정보들 중 식별자가 되는 키의 이름
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 서비스를 구분하는 코드 ex) Github, Naver
        String providerCode = userRequest.getClientRegistration().getRegistrationId();

        // 어떤 소셜로그인을 사용했는지 반환받는 정적 메서드
        ProviderInfo providerInfo = ProviderInfo.from(providerCode);

        // 소셜쪽에서 전달받은 값들을 Map 형태로 받음
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // userNameAttributeName 키의 값. 즉, 식별자값.
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerInfo, attributes);
        String userIdentifier = oAuth2UserInfo.getUserIdentifier();

        User user = getUser(userIdentifier, providerInfo);

        // Security context에 저장할 객체 생성
        return UserPrincipal.builder()
                .user(user)
                .attributes(attributes)
                .userNameAttributeName(userNameAttributeName)
                .build();
    }

    private User getUser(String userIdentifier, ProviderInfo providerInfo) {
        Optional<User> optionalUser = userRepository.findByOAuthInfo(userIdentifier, providerInfo);

        if (optionalUser.isEmpty()) {
            User unregisteredUser = User.builder()
                    .identifier(userIdentifier)
                    .role(Role.NOT_REGISTERED)
                    .providerInfo(providerInfo)
                    .build();
            return userRepository.save(unregisteredUser);
        }
        return optionalUser.get();
    }
}