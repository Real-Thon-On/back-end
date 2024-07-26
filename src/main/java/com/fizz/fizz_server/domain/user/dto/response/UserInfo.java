package com.fizz.fizz_server.domain.user.dto.response;

import lombok.Builder;

@Builder
public record UserInfo(
        Long id,
        String nickname,
        String profileId,
        String profileImage,
        String aboutMe
) {}
