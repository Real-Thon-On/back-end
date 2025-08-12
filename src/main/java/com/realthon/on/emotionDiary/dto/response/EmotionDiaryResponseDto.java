package com.realthon.on.emotionDiary.dto.response;

import com.realthon.on.emotionDiary.entity.EmotionDiary;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
public class EmotionDiaryResponseDto {
        private Long id;
        private Long userId;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public static EmotionDiaryResponseDto fromEntity(EmotionDiary diary) {
                return EmotionDiaryResponseDto.builder()
                        .id(diary.getId())
                        .userId(diary.getUser().getId())
                        .title(diary.getTitle())
                        .content(diary.getContent())
                        .createdAt(diary.getCreatedAt())
                        .modifiedAt(diary.getModifiedAt())
                        .build();
        }}
