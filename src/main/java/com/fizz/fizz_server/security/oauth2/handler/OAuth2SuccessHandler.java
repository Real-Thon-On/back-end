package com.fizz.fizz_server.security.oauth2.handler;

import com.fizz.fizz_server.security.common.enums.Role;
import com.fizz.fizz_server.security.jwt.dto.response.TokenResponseDto;
import com.fizz.fizz_server.security.jwt.service.AuthService;
import com.fizz.fizz_server.security.oauth2.principal.PrincipalDetails;
import com.fizz.fizz_server.user.entity.User;
import com.fizz.fizz_server.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    // TODO : yml 에 해당 경로 정의
    @Value("${oauth2.url.base}")
    private String BASE_URL;

    @Value("${oauth2.url.path.signup}")
    private String SIGNUP_PATH;

    @Value("${oauth2.url.path.auth}")
    private String AUTH_PATH;

    private final UserRepository userRepository;
    private final AuthService tokenService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        User user = principalDetails.getUser();

        // 디바이스 정보 추출
        String deviceId = request.getHeader("Device-Id"); // 커스텀

        String redirectUrl = getRedirectUrlByRole(user, deviceId);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }

    private String getRedirectUrlByRole(User user, String deviceId) {
        Role role = user.getRole();
        Long userId = user.getId();

        /**
         * 회원가입 되지 않은 사용자는 별도의 token 발급 안함
         */
        if (role == Role.NOT_REGISTERED) {
            return UriComponentsBuilder
                    .fromUriString(BASE_URL + SIGNUP_PATH) // fromUriString -> 상대경로 포함 가능
                    .queryParam("id", userId)
                    .build()
                    .toUriString();
        }

        /**
         * 회원가입된 사용자에 대해선 access token 과 refresh token 을 함께 발급
         */
        TokenResponseDto tokenDto = tokenService.issueTokensFor(user, deviceId);


        return UriComponentsBuilder
                .fromHttpUrl(BASE_URL + AUTH_PATH) // fromHttpUrl() -> 절대 경로만 허용
                .queryParam("access", tokenDto.getAccess())
                .queryParam("refresh", tokenDto.getRefresh())
                .queryParam("id", userId)
                .build()
                .toUriString();
    }
}