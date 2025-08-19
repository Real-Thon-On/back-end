package com.realthon.on.ai.groq.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.realthon.on.ai.groq.dto.RecommendedEventDto;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * 일기 분석 API 응답 객체
 * - AI JSON + 사용자용 한국어 답변(replyText) + 내부 추천 이벤트
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiaryAnalyzeResponse {

    /* -------------------- LLM 분석 블록 -------------------- */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Analysis {
        @JsonProperty("dominant_emotion")
        private String dominantEmotion;

        @JsonProperty("emotion_scores")
        private Map<String, Double> emotionScores;

        private double valence;
        private double arousal;
        private int temperature;
        private String weather;

        @JsonProperty("color_hex")
        private String colorHex;

        private List<String> keywords;

        @JsonProperty("risk_flag")
        private boolean riskFlag;
    }

    /* 하이라이트(선택) — 프론트 카드용 */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Highlights {
        @JsonProperty("good_points")
        private List<String> goodPoints;

        @JsonProperty("improvements")
        private List<String> improvements;
    }

    /* -------------------- LLM 제안 블록 -------------------- */
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Suggestions {
        private String good;
        private String suggest;
        private String feedback;
        @JsonProperty("total_summary")
        private String totalSummary;
    }


    /* -------------------- 루트 응답 필드 -------------------- */
    private Analysis analysis;                 // LLM 분석
    private Suggestions suggestions;           // LLM 제안 (없으면 폴백으로 채움)
    private String replyText;                  // LLM 한국어 본문 (빈 줄 이후)

    @Builder.Default
    private List<RecommendedEventDto> recommendedEvents = List.of(); // 내부 전시회 추천
}
