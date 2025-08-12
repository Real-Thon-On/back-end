package com.realthon.on.emotionDiary.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


public class EmotionDiaryRequestDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddEmotionDiaryRequestDto {
        private Long userId;
        @NotBlank(message = "제목은 반드시 입력해야 합니다.")
        private String title;
        private String content;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateEmotionDiaryRequestDto {
        @NotBlank(message = "제목은 반드시 입력해야 합니다.")
        private String title;
        private String content;
    }
}

