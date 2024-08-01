package com.fizz.fizz_server.domain.file.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PreSignedUrlCreateRequest(
        @NotBlank
        String uploadId,
        @NotNull
        Long partNumber,
        @NotBlank
        String key
) {
}
