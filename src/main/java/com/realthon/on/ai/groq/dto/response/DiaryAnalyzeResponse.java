package com.realthon.on.ai.groq.dto.response;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 일기 분석 API 응답 객체
 * - AI 모델이 반환한 감정 분석 결과(JSON) + 한국어 사용자 답변(replyText)을 함께 담음
 */
@Data
public class DiaryAnalyzeResponse {

    /**
     * 감정 분석 결과
     */
    @Data
    public static class Analysis {
        private String dominant_emotion;        // 지배적인 감정 (예: joy, sad, anxiety 등)
        private Map<String, Double> emotion_scores; // 각 감정별 점수 (0.0 ~ 1.0 범위)
        private double valence;                 // 정서 가치 (긍정/부정 정도, -1.0 ~ +1.0)
        private double arousal;                 // 각성도 (흥분/차분 정도, 0.0 ~ 1.0)
        private int temperature;                // 감정 온도 (0 ~ 100, arousal 기반)
        private String weather;                 // 감정 날씨 비유 (SUNNY, CLOUDY, RAIN, STORM)
        private String color_hex;               // 감정을 시각화하는 색상 (HEX 코드, 예: #7FAAFF)
        private List<String> keywords;          // 주요 키워드 (한글 단어 리스트)
        private boolean risk_flag;              // 위험 신호 여부 (자해/타해 관련 언급 시 true)
    }

    /**
     * 사용자를 위한 제안 메시지
     */
    @Data
    public static class Suggestions {
        private String reframing;          // 상황을 다른 시각에서 볼 수 있도록 돕는 문장
        private String encouragement;      // 구체적인 격려 문장
        private String gentle_action_tip;  // 5~10분 내에 실천 가능한 작은 행동 제안
        private String local_cue;          // 사용자의 지역 기반 힌트 (없으면 빈 문자열)
    }

    private Analysis analysis;       // 감정 분석 데이터
    private Suggestions suggestions; // 사용자 맞춤 제안 데이터
    private String replyText;        // JSON 이후 생성되는 한국어 응답 텍스트 (5~9문장)
}
