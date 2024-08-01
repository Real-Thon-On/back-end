package com.fizz.fizz_server.domain.file.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record PreSignedUploadInitiateRequest(
        @NotBlank(message = "원본 파일 이름은 빈 값일 수 없습니다.")
        String originalFileName,
        @NotBlank(message = "파일 포맷명은 빈 값일 수 없습니다.")
        String fileFormat,
        @Max(value = 1024, message = "파일 크기는 1GB를 초과할 수 없습니다.") // 1024MB 이하로 제한
        Long fileSize
) {
}