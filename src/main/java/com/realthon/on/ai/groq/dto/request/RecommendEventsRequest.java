package com.realthon.on.ai.groq.dto.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendEventsRequest {
    /** 추천에 사용할 키워드 목록 (한글 가능) */
    private List<String> keywords;

    /** 추천 개수 (기본 5) */
    @Builder.Default
    private int limit = 5;
}