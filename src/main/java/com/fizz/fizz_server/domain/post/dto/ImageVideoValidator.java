package com.fizz.fizz_server.domain.post.dto;

import com.fizz.fizz_server.domain.post.dto.request.PostRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageVideoValidator implements ConstraintValidator<ValidPostRequest, PostRequest> {

    @Override
    public boolean isValid(PostRequest postRequest, ConstraintValidatorContext context) {
        // images와 video가 모두 null이거나 비어있지 않으면 유효하지 않음
        boolean hasImages = postRequest.images() != null && !postRequest.images().isEmpty();
        boolean hasVideo = postRequest.video() != null && !postRequest.video().isEmpty();

        // 둘 중 하나만 존재해야 유효함
        return (hasImages || hasVideo) && !(hasImages && hasVideo);
    }
}