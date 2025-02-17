package com.fizz.fizz_server.domain.example.user.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserDetailInfoResponse (
        Long id,
        String nickname,
        String email,
        String profileId,
        String profileImage,
        String aboutMe,
        List<UserInfo> follower,
        List<UserInfo> following
) {}