package com.fizz.fizz_server.global.oauth2.handler;

import com.fizz.fizz_server.domain.user.domain.RoleType;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import com.fizz.fizz_server.global.jwt.TokenProvider;
import com.fizz.fizz_server.global.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.fizz.fizz_server.global.oauth2.service.OAuth2UserPrincipal;
import com.fizz.fizz_server.global.oauth2.user.OAuth2Provider;
import com.fizz.fizz_server.global.oauth2.user.unlink.OAuth2UserUnlinkManager;
import com.fizz.fizz_server.global.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static com.fizz.fizz_server.global.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static com.fizz.fizz_server.global.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to {}", targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(getDefaultTargetUrl());

        String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");

        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        if (principal == null) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login failed")
                    .build().toUriString();
        }

        if (mode.equalsIgnoreCase("login")) {
            String providerId = principal.getUserInfo().getId();
            OAuth2Provider provider = principal.getUserInfo().getProvider();

            // 새로운 유저인지 전달 - 우선 쿼리 파라미터에 담지만, 방식 변경 가능
            AtomicReference<Boolean> isNewUser = new AtomicReference<>(false);

            User user = userRepository.findByProviderAndProviderId(provider, providerId)
                    .orElseGet(() -> {
                        isNewUser.set(true);
                        return createAndSaveNewUser(provider, providerId);
                    });

            if (user.getRole() == RoleType.ROLE_GUEST) {
                isNewUser.set(true);
            }

            String accessToken = tokenProvider.createToken(user.getId(), user.getRole());
            // 리프레시 토큰 발급, 리프레시 토큰 DB 저장

            log.info("email={}, nickname={}, accessToken={}",
                    principal.getUserInfo().getEmail(),
                    principal.getUserInfo().getNickname(),
                    principal.getUserInfo().getAccessToken()
            );

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("is_new_user", isNewUser)
                    .queryParam("access_token", accessToken)
                    // 리프레시 토큰
                    .build().toUriString();
        } else if (mode.equalsIgnoreCase("unlink")) {
            String accessToken = principal.getUserInfo().getAccessToken();
            String providerId = principal.getUserInfo().getId();
            OAuth2Provider provider = principal.getUserInfo().getProvider();

            User user = userRepository.findByProviderAndProviderId(provider, providerId)
                    .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

            userRepository.delete(user);

            oAuth2UserUnlinkManager.unlink(provider, accessToken);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build().toUriString();
        }
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString();
    }

    private User createAndSaveNewUser(OAuth2Provider provider, String providerId) {
        User user = User.builder()
                .provider(provider)
                .providerId(providerId)
                .role(RoleType.ROLE_GUEST)
                .build();

        return userRepository.save(user);
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
