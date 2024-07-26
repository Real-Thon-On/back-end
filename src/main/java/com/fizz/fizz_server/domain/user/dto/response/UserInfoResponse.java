package com.fizz.fizz_server.domain.user.dto.response;

import lombok.Builder;

@Builder
public record UserInfoResponse (
        String nickname,
        String profileId,
        String profileImage,
        String aboutMe
) {}
