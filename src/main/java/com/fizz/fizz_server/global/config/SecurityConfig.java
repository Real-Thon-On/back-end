package com.fizz.fizz_server.global.config;

import com.fizz.fizz_server.global.jwt.CustomAccessDeniedHandler;
import com.fizz.fizz_server.global.jwt.CustomAuthenticationEntryPoint;
import com.fizz.fizz_server.global.jwt.JwtAuthorizationFilter;
import com.fizz.fizz_server.global.jwt.TokenProvider;
import com.fizz.fizz_server.global.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.fizz.fizz_server.global.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.fizz.fizz_server.global.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.fizz.fizz_server.global.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return hierarchy;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, TokenProvider tokenProvider, AuthenticationManager authenticationManager) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST, "/api/user/login-info").hasRole("GUEST") // 오직 첫   로그인 유저
                        .requestMatchers(HttpMethod.GET, "/api/user/me").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/user/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/**").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/user/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/user/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/files/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/posts/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/challenge/**").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/challenge/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/category/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/category").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/category/user").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/category/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/comment/**").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/comment/**").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/comment/**").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/comment/**").permitAll()
                        .anyRequest().hasRole("ADMIN"))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(configure ->
                        configure.authorizationEndpoint(config ->
                                        config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                                                .baseUri("/api/user/oauth2"))
                                .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler))
                .addFilterBefore(new JwtAuthorizationFilter(tokenProvider, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
        ;

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(TokenProvider tokenProvider) {
        return new ProviderManager(tokenProvider);
    }
}
