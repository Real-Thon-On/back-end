package com.fizz.fizz_server.domain.file.dto.request;

import java.util.List;

public record FinishUploadRequest(
        String uploadId,
        String key,
        List<Part> parts
) {
    public record Part(
            int partNumber,
            String eTag
    ) {
    }
}
