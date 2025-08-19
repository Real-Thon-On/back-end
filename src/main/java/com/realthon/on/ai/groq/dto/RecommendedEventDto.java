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
    private String mainCategory;    // 있으면 노출용
    private String matchedKeywords;
    private String imageUrl;
}