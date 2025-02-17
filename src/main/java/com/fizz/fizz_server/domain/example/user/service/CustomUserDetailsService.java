package com.fizz.fizz_server.domain.example.user.service;

import com.fizz.fizz_server.domain.example.user.domain.CustomUserPrincipal;
import com.fizz.fizz_server.domain.example.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findById(Long.valueOf(userId))
                .map(CustomUserPrincipal::new)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));
    }
}
