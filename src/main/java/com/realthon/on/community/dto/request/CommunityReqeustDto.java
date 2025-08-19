package com.realthon.on.community.dto.request;

import com.realthon.on.community.entity.HashTagType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

public class CommunityReqeustDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddBaordRequestDto {
        @NotBlank(message = "제목은 반드시 입력해야 합니다.")
        private String title;
        private String content;
        private Set<HashTagType> hashtags;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateBoardRequestDto {
        @NotBlank(message = "제목은 반드시 입력해야 합니다.")
        private String title;
        private String content;
        private Set<HashTagType> hashtags;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddCommentRequestDto {
        @NotBlank(message = "내용은 반드시 입력해야 합니다.")
        private String content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateCommentRequestDto {
        @NotBlank(message = "내용은 반드시 입력해야 합니다.")
        private String content;
    }






}

