package com.realthon.on.ai.groq.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class DiaryAnalyzeRequest {
    private String todayDate;              // 예: 2025-08-17
    private String userRegion;             // 예: 서울 종로구
    private String recentEntriesSummary;   // 없으면 빈 문자열
    private String userDiaryText;          // 일기 원문
}
