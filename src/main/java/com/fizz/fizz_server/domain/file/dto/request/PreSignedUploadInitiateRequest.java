package com.fizz.fizz_server.domain.file.dto.request;

public record PreSignedUploadInitiateRequest(
        String originalFileName,
        String fileType,
        Long fileSize
) {
}