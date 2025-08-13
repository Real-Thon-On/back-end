package com.realthon.on.emotionDiary.dto.response;

import com.realthon.on.emotionDiary.entity.EmotionDiary;
import com.realthon.on.emotionDiary.entity.WeatherType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class EmotionDiaryResponseDto {
        private Long id;
        private Long userId;
        private LocalDate date;
        private WeatherType weather;
        private Set<String> hashtags;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public static EmotionDiaryResponseDto fromEntity(EmotionDiary diary) {
                return EmotionDiaryResponseDto.builder()
                        .id(diary.getId())
                        .userId(diary.getUser().getId())
                        .date(diary.getDate())
                        .weather(diary.getWeather())
                        .hashtags(diary.getHashtags())
                        .content(diary.getContent())
                        .createdAt(diary.getCreatedAt())
                        .modifiedAt(diary.getModifiedAt())
                        .build();
        }}
