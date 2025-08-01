package com.fizz.fizz_server.security.common.config;

import com.fizz.fizz_server.security.jwt.filter.JwtAuthenticationFilter;
import com.fizz.fizz_server.security.jwt.service.CustomUserDetailsService;
import com.fizz.fizz_server.security.jwt.util.JwtTokenProvider;
import com.fizz.fizz_server.security.oauth2.handler.OAuth2FailureHandler;
import com.fizz.fizz_server.security.oauth2.handler.OAuth2SuccessHandler;
import com.fizz.fizz_server.security.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();  // 비밀번호 암호화에 BCryptPasswordEncoder 사용
//    }




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**","/api/user/sign-up").permitAll()         // 로그인, 회원가입
                        .anyRequest().permitAll()
                )

                // oauth2 설정
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(c -> c.userService(customOAuth2UserService)) // 사용자 정보를 어디서 load 할지 설정
                        .successHandler(oAuth2SuccessHandler) // 로그인 성공 시 핸들러
                        .failureHandler(oAuth2FailureHandler) // 로그인 실패 시 핸들러
                )

                // jwt 설정
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);  // JWT 인증 필터 추가;

        return http.build();
    }


}