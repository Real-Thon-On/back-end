package com.fizz.fizz_server.domain.user.service;

import com.fizz.fizz_server.domain.user.domain.Follow;
import com.fizz.fizz_server.domain.user.domain.RoleType;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.dto.request.UserInfoUpdateRequest;
import com.fizz.fizz_server.domain.user.dto.response.CheckProfileIdResponse;
import com.fizz.fizz_server.domain.user.dto.response.UserDetailInfoResponse;
import com.fizz.fizz_server.domain.user.dto.response.UserInfo;
import com.fizz.fizz_server.domain.user.repository.FollowRepository;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
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
    private final FollowRepository followRepository;

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

    public UserDetailInfoResponse getUserOwnInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        return getUserInfo(user);
    }

    public UserDetailInfoResponse getUserInfoByProfileId(String profileId) {
        User user = userRepository.findByProfileId(profileId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        return getUserInfo(user);
    }

    private UserDetailInfoResponse getUserInfo(User user) {
        List<UserInfo> follower = convertToUserInfo(followRepository.findByFollowee(user));
        List<UserInfo> following = convertToUserInfo(followRepository.findByFollower(user));

        return UserDetailInfoResponse.builder()
                .id(user.getId())
                .profileId(user.getProfileId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .aboutMe(user.getAboutMe())
                .follower(follower)
                .following(following)
                .build();
    }

    private List<UserInfo> convertToUserInfo(List<User> userList) {
        return userList.stream()
                .map(user -> UserInfo.builder()
                        .id(user.getId())
                        .profileId(user.getProfileId())
                        .nickname(user.getNickname())
                        .profileImage(user.getProfileImage())
                        .aboutMe(user.getAboutMe())
                        .build())
                .toList();
    }

    @Transactional
    public void followUser(Long followerId, Long followeeId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        if (follower == followee) {
            throw new BusinessException(ExceptionType.SELF_FOLLOW);
        }

        followRepository.findByFollowerAndFollowee(follower, followee)
                .ifPresent(it -> {
                    throw new BusinessException(ExceptionType.ALREADY_FOLLOW);
                });

        Follow newFollow = Follow.builder()
                .follower(follower)
                .followee(followee)
                .build();

        followRepository.save(newFollow);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followeeId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        Follow deleteFollow = followRepository.findByFollowerAndFollowee(follower, followee)
                .orElseThrow(() -> new BusinessException(ExceptionType.FOLLOW_NOT_FOUND));

        followRepository.delete(deleteFollow);
    }

}
