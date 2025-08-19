package com.realthon.on.emotionDiary.dto.request;

import com.realthon.on.emotionDiary.entity.WeatherType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;


public class EmotionDiaryRequestDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddEmotionDiaryRequestDto {
        private LocalDate date;
        private WeatherType weather;
        private Set<String> hashtags;
        @Size(max = 500, message = "내용은 500자 이내여야 합니다.")
        @NotBlank(message = "내용은 반드시 입력해야 합니다.")
        private String content;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateEmotionDiaryRequestDto {
        private LocalDate date;
        private WeatherType weather;
        private Set<String> hashtags;
        @Size(max = 500, message = "내용은 500자 이내여야 합니다.")
        @NotBlank(message = "내용은 반드시 입력해야 합니다.")
        private String content;
    }
}

