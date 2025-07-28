package com.fizz.fizz_server.security.filter;



import com.fizz.fizz_server.security.service.CustomUserDetailsService;
import com.fizz.fizz_server.security.util.CustomUserDetails;
import com.fizz.fizz_server.security.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = extractToken(request);  // JWT 토큰 추출

        log.info("token: {}",token);

        if (token != null && !jwtTokenProvider.isTokenExpired(token)) {
            String username = jwtTokenProvider.extractUsername(token);  // JWT에서 사용자명 추출

            log.info("username: {}",username);

            // UserDetailsService를 통해 사용자 정보 로드
            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

            // 인증 객체 생성 및 SecurityContext에 설정
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

            log.info("userDetails.getAuthorities(): {}", userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);  // "Bearer "를 제외한 토큰 값 반환
        }
        return null;
    }
}
