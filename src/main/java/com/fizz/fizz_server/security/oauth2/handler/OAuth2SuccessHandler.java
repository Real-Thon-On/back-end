package com.fizz.fizz_server.security.oauth2.handler;

import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import com.fizz.fizz_server.security.common.enums.Role;
import com.fizz.fizz_server.user.entity.User;
import com.fizz.fizz_server.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth2.url.base}")
    private String BASE_URL;

    @Value("${oauth2.url.path.signup}")
    private String SIGNUP_URL;

    @Value("${oauth2.url.path.auth}")
    private String AUTH_URL;

    @Autowired
    private final UserRepository userRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String identifier = oAuth2User.getName();

        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));


        String redirectUrl = getRedirectUrlByRole(user.getRole(), identifier);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String getRedirectUrlByRole(Role role, String identifier) {
        if (role == Role.NOT_REGISTERED) {
            return UriComponentsBuilder.fromUriString(SIGNUP_URL)
                    .queryParam("identifier", identifier)
                    .build()
                    .toUriString();
        }

        return UriComponentsBuilder.fromHttpUrl(AUTH_URL)
                .queryParam("identifier", identifier)
                .build()
                .toUriString();
    }
}