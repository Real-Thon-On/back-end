package com.fizz.fizz_server.domain.example.user.service;

import com.fizz.fizz_server.domain.example.user.domain.RoleType;
import com.fizz.fizz_server.domain.example.user.domain.User;
import com.fizz.fizz_server.domain.example.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void setProfileIdAndEmail(Long userId, String profileId, String email) {
        userRepository.findByProfileId(profileId)
                .ifPresent(it -> {
                    throw new BusinessException(ExceptionType.DUPLICATED_PROFILE_ID);
                });

        userRepository.findByEmail(email)
                .ifPresent(it -> {
                    throw new BusinessException(ExceptionType.DUPLICATED_EMAIL);
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        user.setNickname(profileId);
        user.setProfileId(profileId);
        user.setEmail(email);
        user.setRole(RoleType.ROLE_USER);
    }



}
