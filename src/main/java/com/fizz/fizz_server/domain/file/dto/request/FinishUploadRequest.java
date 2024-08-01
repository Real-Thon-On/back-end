package com.fizz.fizz_server.domain.file.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FinishUploadRequest(
        @NotBlank
        String uploadId,
        @NotBlank
        String key,
        @NotNull
        List<Part> parts
) {
    public record Part(
            int partNumber,
            String eTag
    ) {
    }
}
