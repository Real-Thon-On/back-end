package com.fizz.fizz_server.domain.user.service;

import com.fizz.fizz_server.domain.user.domain.RoleType;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.dto.request.UserInfoUpdateRequest;
import com.fizz.fizz_server.domain.user.dto.response.CheckProfileIdResponse;
import com.fizz.fizz_server.domain.user.dto.response.UserInfoResponse;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void updateUserInfo(Long userId, UserInfoUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        if (!user.getEmail().equals(request.getEmail())) {
            userRepository.findByEmail(request.getEmail())
                    .ifPresent(it -> {
                        throw new BusinessException(ExceptionType.DUPLICATED_EMAIL);
                    });

            user.setEmail(request.getEmail());
        }

        if (!user.getProfileId().equals(request.getProfileId())) {
            userRepository.findByProfileId(request.getProfileId())
                    .ifPresent(it -> {
                        throw new BusinessException(ExceptionType.DUPLICATED_PROFILE_ID);
                    });

            user.setProfileId(request.getProfileId());
        }

        user.setNickname(request.getNickname());
        user.setProfileImage(request.getProfileImage());
        user.setAboutMe(request.getAboutMe());
    }

    @Transactional(readOnly = true)
    public CheckProfileIdResponse isProfileIdDuplicate(String profileId) {
        try {
            userRepository.findByProfileId(profileId)
                    .ifPresent(it -> {
                        throw new BusinessException(ExceptionType.DUPLICATED_PROFILE_ID);
                    });
        } catch (BusinessException e) {
            return new CheckProfileIdResponse(true);
        }
        return new CheckProfileIdResponse(false);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getProfileInfo(String profileId) {
        User user = userRepository.findByProfileId(profileId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        return UserInfoResponse.builder()
                .profileId(user.getProfileId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .aboutMe(user.getAboutMe())
                .build();
    }
}
