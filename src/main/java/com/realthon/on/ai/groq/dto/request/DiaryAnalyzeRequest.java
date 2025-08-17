package com.realthon.on.ai.groq.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class DiaryAnalyzeRequest {
    private String todayDate;       // 예: 2025-08-17
    private String userDiaryText;   // 일기 원문
}
