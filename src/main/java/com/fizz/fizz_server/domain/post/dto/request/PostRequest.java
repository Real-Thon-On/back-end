package com.fizz.fizz_server.domain.post.dto.request;

import com.fizz.fizz_server.domain.post.dto.ValidPostRequest;

import java.util.List;

@ValidPostRequest
public record PostRequest(
        String title,
        String content,
        List<String> images,
        String video
) {
}
