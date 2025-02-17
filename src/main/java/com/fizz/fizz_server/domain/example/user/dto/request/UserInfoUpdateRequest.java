package com.fizz.fizz_server.domain.example.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserInfoUpdateRequest {
    @NotBlank(message = "email은 빈값일 수 없습니다.")
    private String email;
    @NotBlank(message = "nickname은 빈값일 수 없습니다.")
    private String nickname;
    @NotBlank(message = "profileId는 빈값일 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_.]+$",
            message = "profileId는 영문, 숫자, 밑줄 및 마침표만 포함할 수 있습니다.")
    private String profileId;
    private String profileImage;
    private String aboutMe;
}
