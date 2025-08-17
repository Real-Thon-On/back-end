package com.realthon.on.ai.groq.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendedEventDto {
    private Long id;
    private String title;
    private String url;
    private Integer imageCount;   // 있으면 노출용
    private String mainCategory;  // 있으면 노출용
    private double score;         // 내부 점수(디버그/정렬용)
    private String matchedKeywords; // 어떤 키워드가 매칭됐는지
}