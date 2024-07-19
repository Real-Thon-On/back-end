package com.fizz.fizz_server.domain.file.dto.request;

public record PreSignedUrlCreateRequest(
        String uploadId,
        Long partNumber,
        String key
) {
}
