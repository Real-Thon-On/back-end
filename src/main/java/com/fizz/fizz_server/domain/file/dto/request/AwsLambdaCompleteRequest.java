package com.fizz.fizz_server.domain.file.dto.request;

import java.util.List;

public record AwsLambdaCompleteRequest(
        String type,
        List<String> urls
) {
}