package com.realthon.on.ai.groq.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realthon.on.ai.groq.dto.RecommendedEventDto;
import com.realthon.on.ai.groq.dto.request.DiaryAnalyzeRequest;
import com.realthon.on.ai.groq.dto.request.EvaluateHarmfulnessRequest;
import com.realthon.on.ai.groq.dto.request.RecommendEventsRequest;
import com.realthon.on.ai.groq.dto.response.DiaryAnalyzeResponse;
import com.realthon.on.ai.groq.dto.response.EvaluateHarmfulnessResponse;
import com.realthon.on.ai.groq.dto.response.RecommendEventsResponse;
import com.realthon.on.events.entity.LocalArtEvent;
import com.realthon.on.events.repository.LocalArtEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

import static io.micrometer.common.util.StringUtils.isBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroqApiService {

    private final RestTemplate restTemplate;
    private final LocalArtEventRepository eventRepository;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.model}")
    private String model;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /* -------------------- 유해성 평가 -------------------- */
    public EvaluateHarmfulnessResponse evaluateHarmfulness(EvaluateHarmfulnessRequest request) {
        String url = "https://api.groq.com/openai/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String requestJson =
                "{\"messages\": [{\"role\": \"user\", \"content\": \" '"
                        + request.getSentence()
                        + "' Return only a number from 0 to 10 indicating the degree of harmfulness of this text, "
                        + "where a larger number represents greater harmfulness, without any additional explanation.\"}], "
                        + "\"model\": \"" + this.model + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        String raw = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();

        int result = -1; // 파싱 실패 시 fallback
        try {
            JsonNode root = MAPPER.readTree(raw);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            result = Integer.parseInt(content.trim().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            log.warn("Failed to parse harmfulness response, raw={}", raw, e);
        }

        return EvaluateHarmfulnessResponse.builder().degree(result).build();
    }

    /* -------------------- 일기 분석 -------------------- */
    public DiaryAnalyzeResponse analyzeDiary(DiaryAnalyzeRequest req) {
        String url = "https://api.groq.com/openai/v1/chat/completions";

        String systemPrompt =
                "You are an emotional analyst and empathetic counselor specialized in analyzing Korean diary texts.\n" +
                        "Goals:\n" +
                        "1) Extract the dominant emotion, emotion intensity scores, valence/arousal values, and main keywords.\n" +
                        "2) Derive a weather metaphor (SUNNY/CLOUDY/RAIN/STORM), an emotional temperature (0~100), and a representative color (hex).\n" +
                        "3) Provide short Korean bullets for good_points(<=3) and improvements(<=3), each item <= 40 chars.\n" +
                        "4) Provide SUGGESTIONS with four fields (reframing, encouragement, gentle_action_tip, local_cue). All sentences must end with '입니다.'.\n" +
                        "5) Do not provide medical or legal diagnoses. If any signs of self-harm or harm to others appear, set risk_flag=true and recommend seeking help from professionals.\n" +
                        "6) Tone: warm but calm Korean; every sentence ends with '입니다.'; no emojis, no exaggerations, no casual tone.\n" +
                        "7) Output format is STRICT: first JSON only, then ONE blank line, then a user-facing reply in Korean (5–9 sentences).\n\n" +
                        "Visualization rules:\n" +
                        "- temperature = round(arousal * 100)\n" +
                        "- weather: valence ≥ 0.4 → SUNNY, -0.4 < valence < 0.4 → CLOUDY, valence ≤ -0.4 && arousal < 0.5 → RAIN, valence ≤ -0.4 && arousal ≥ 0.5 → STORM\n" +
                        "- color_hex examples: sadness/anger #E38B8B, neutral #9FA8B3, calm/joy #7FAAFF\n\n" +
                        "Output schema (JSON):\n" +
                        "{\n" +
                        "  \"analysis\": {\n" +
                        "    \"dominant_emotion\": \"joy|sad|anger|fear|anxiety|calm|tired|lonely|hope etc.\",\n" +
                        "    \"emotion_scores\": { \"joy\": 0.00, \"sad\": 0.00, \"anger\": 0.00, \"fear\": 0.00, \"anxiety\": 0.00, \"calm\": 0.00, \"tired\": 0.00, \"lonely\": 0.00, \"hope\": 0.00 },\n" +
                        "    \"valence\": -1.0,\n" +
                        "    \"arousal\": 0.0,\n" +
                        "    \"temperature\": 0,\n" +
                        "    \"weather\": \"SUNNY|CLOUDY|RAIN|STORM\",\n" +
                        "    \"color_hex\": \"#7FAAFF\",\n" +
                        "    \"keywords\": [\"up to 6 Korean words\"],\n" +
                        "    \"risk_flag\": false\n" +
                        "  },\n" +
                        "  \"highlights\": {\n" +
                        "    \"good_points\": [\"문장 최대 40자, 최대 3개\"],\n" +
                        "    \"improvements\": [\"문장 최대 40자, 최대 3개\"]\n" +
                        "  },\n" +
                        "  \"overall_opinion\": \"2~4문장 한국어\",\n" +
                        "  \"suggestions\": {\n" +
                        "    \"reframing\": \"부드러운 재구성 한 문장, '입니다.'로 끝남\",\n" +
                        "    \"encouragement\": \"구체적 격려 한 문장, '입니다.'로 끝남\",\n" +
                        "    \"gentle_action_tip\": \"5~10분 내 실행 가능한 작고 구체적 제안 한 문장, '입니다.'로 끝남\",\n" +
                        "    \"local_cue\": \"지역 힌트 한 문장 또는 빈 문자열\"\n" +
                        "  }\n" +
                        "}\n\n" +
                        "Rules for suggestions:\n" +
                        "- All four fields MUST be present.\n" +
                        "- Empty or null is NOT allowed except local_cue may be an empty string.\n\n" +
                        "After the JSON, insert one blank line, then generate the user-facing Korean reply (5–9 sentences). Include: 1–2 empathy sentences, 1 emotional labeling sentence, 1 gentle reframing sentence, 1 encouragement sentence, and 1 tiny action suggestion (5–10 minutes), all ending with '입니다.'.";

        String userPrompt = String.format(Locale.ROOT,
                "다음은 사용자의 일기입니다.\n\n" +
                        "- 작성일: %s\n" +
                        "- 현재 일기 원문:\n" +
                        "```\n%s\n```\n\n" +
                        "위 지침에 따라 먼저 JSON만 출력하고, 빈 줄 후 사용자용 답변 텍스트를 출력하세요.",
                safe(req.getTodayDate()),
                safe(req.getUserDiaryText())
        );

        Map<String, Object> body = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        messages.add(Map.of("role", "user", "content", userPrompt));

        body.put("model", model);
        body.put("messages", messages);
        body.put("temperature", 0.7);
        body.put("max_tokens", 1200);
        body.put("top_p", 1);
        body.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson;
        try { requestJson = MAPPER.writeValueAsString(body); }
        catch (Exception e) { throw new RuntimeException("Failed to serialize request body", e); }

        ResponseEntity<String> resp = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<>(requestJson, headers), String.class);

        String raw = Optional.ofNullable(resp.getBody()).orElse("");
        String content = extractAssistantContent(raw);

        // 1) LLM 결과 파싱
        DiaryAnalyzeResponse out = splitJsonAndText(content);

        normalizeVisualization(out.getAnalysis());


        if (out.getSuggestions() == null
                || isBlank(out.getSuggestions().getReframing())
                || isBlank(out.getSuggestions().getEncouragement())
                || isBlank(out.getSuggestions().getGentleActionTip())) {
            out.setSuggestions(fallbackSuggestions(out.getAnalysis(), out.getReplyText()));
        }

        // 2) 추천 이벤트(전시회) 내부 호출로 부착
        try {
            List<String> keywords = Optional.ofNullable(out.getAnalysis())
                    .map(DiaryAnalyzeResponse.Analysis::getKeywords)
                    .orElse(List.of());

            RecommendEventsRequest rreq = RecommendEventsRequest.builder()
                    .keywords(keywords)
                    .limit(3) // 필요 개수
                    .build();

            RecommendEventsResponse rres = recommendEvents(rreq);
            out.setRecommendedEvents(
                    (rres != null && rres.getItems() != null) ? rres.getItems() : List.of()
            );
        } catch (Exception e) {
            log.warn("attach recommendedEvents failed", e);
            out.setRecommendedEvents(List.of());
        }

        return out;
    }
    private void normalizeVisualization(DiaryAnalyzeResponse.Analysis a){
        if (a == null) return;
        a.setTemperature((int)Math.round(a.getArousal() * 100.0));
        double v = a.getValence(), ar = a.getArousal();
        String weather;
        if (v >= 0.4) weather = "SUNNY";
        else if (v > -0.4) weather = "CLOUDY";
        else weather = (ar >= 0.5) ? "STORM" : "RAIN";
        a.setWeather(weather);
    }

    private DiaryAnalyzeResponse.Suggestions fallbackSuggestions(
            DiaryAnalyzeResponse.Analysis a, String replyText) {

        String emo = (a != null && a.getDominantEmotion() != null) ? a.getDominantEmotion() : "감정";
        String reframing = switch (emo) {
            case "불안", "anxiety" -> "불안은 준비하려는 마음의 신호이므로 작은 단계를 정리하면 통제감이 회복됩니다.";
            case "슬픔", "sad"     -> "슬픔은 잃은 것의 가치를 보여주는 감정이므로 스스로를 돌보는 시간이 필요합니다.";
            case "분노", "anger"   -> "분노는 경계가 필요함을 알려주므로 사실과 요구를 구분해 정리하면 명확해집니다.";
            case "외로움", "lonely"-> "외로움은 연결을 원하는 신호이므로 가벼운 대화를 시작하는 것만으로도 도움이 됩니다.";
            case "지침", "tired"   -> "피로는 회복이 우선이므로 짧은 휴식 후 우선순위를 재정리하면 효과적입니다.";
            default                -> "현재 감정은 의미 있는 신호이므로 작은 단계를 정해 다루면 상황이 정돈됩니다.";
        };

        var s = new DiaryAnalyzeResponse.Suggestions();
        s.setReframing(ensureEndsWithImnida(reframing));
        s.setEncouragement(ensureEndsWithImnida("이미 어려움 속에서도 기록으로 정리했고 이는 회복의 시작입니다."));
        s.setGentleActionTip(ensureEndsWithImnida("5분간 복식호흡 후 오늘 할 일 한 가지를 메모장에 한 줄로 적습니다."));
        s.setLocalCue(""); // 지역 데이터 있을 때만 채우십시오.
        return s;
    }


    private static String extractAssistantContent(String raw) {
        try {
            JsonNode root = MAPPER.readTree(raw);
            JsonNode choices = root.path("choices");
            if (choices.isArray() && choices.size() > 0) {
                return choices.get(0).path("message").path("content").asText("");
            }
        } catch (Exception e) {
            log.warn("Failed to parse Groq response JSON", e);
        }
        return "";
    }
    private String ensureEndsWithImnida(String s) {
        if (s == null || s.isBlank()) return "";
        String t = s.trim();
        return t.endsWith("입니다.") ? t : (t.endsWith("입니다") ? t + "." : t + "입니다.");
    }

    private boolean isBlank(String s){ return s == null || s.trim().isEmpty(); }

    private static DiaryAnalyzeResponse splitJsonAndText(String content) {
        DiaryAnalyzeResponse out = DiaryAnalyzeResponse.builder().build();
        if (content == null) content = "";

        int firstBrace = content.indexOf('{');
        int lastBrace = content.lastIndexOf('}');
        String jsonPart = "";
        String textPart = "";

        if (firstBrace >= 0 && lastBrace > firstBrace) {
            jsonPart = content.substring(firstBrace, lastBrace + 1).trim();
            textPart = content.substring(lastBrace + 1).trim();
        } else {
            out.setReplyText(content.trim());
            return out;
        }

        try {
            JsonNode node = MAPPER.readTree(jsonPart);

            // analysis / suggestions (기존)
            DiaryAnalyzeResponse.Analysis analysis =
                    MAPPER.treeToValue(node.path("analysis"), DiaryAnalyzeResponse.Analysis.class);
            out.setAnalysis(analysis);

            DiaryAnalyzeResponse.Suggestions suggestions =
                    MAPPER.treeToValue(node.path("suggestions"), DiaryAnalyzeResponse.Suggestions.class);
            out.setSuggestions(suggestions);

       } catch (Exception e) {
            log.warn("Failed to parse analysis JSON part", e);
        }

        out.setReplyText(textPart.replaceAll("^\\s+", "").replaceAll("\\s+$", ""));
        return out;
    }

    private static String safe(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", " ")
                .replace("\n", "\\n");
    }

    /* -------------------- 추천 (키워드 기반) -------------------- */
    public RecommendEventsResponse recommendEvents(RecommendEventsRequest req) {
        int limit = Math.max(1, req.getLimit());
        List<String> keywords = (req.getKeywords() == null) ? List.of() : req.getKeywords();

        var events = eventRepository.findSoonestOrderByEndDateNullsLast(PageRequest.of(0, 100));
        if (events.isEmpty()) return RecommendEventsResponse.builder().items(List.of()).build();

        // 카탈로그(간단 JSON lines)
        StringBuilder catalog = new StringBuilder();
        for (LocalArtEvent e : events) {
            catalog.append(String.format(
                    "- {\"id\": %d, \"title\": \"%s\", \"url\": \"%s\", \"mainCategory\": \"%s\"}\n",
                    e.getId(),
                    jsonEscape(nz(e.getTitle()) ? e.getTitle() : ""),
                    jsonEscape(nz(e.getUrl()) ? e.getUrl() : ""),
                    jsonEscape(nz(e.getMainCategory()) ? e.getMainCategory() : "")
            ));
        }

        // 프롬프트: reason 반드시 채우기!
        String systemPrompt =
                "You are a recommender for cultural events in Korea. " +
                        "Use given keywords to pick TOP-N events from the catalog. " +
                        "Return STRICT JSON only with the exact schema: " +
                        "{\"items\":[{\"id\":<long>,\"reason\":\"short Korean reason including matched keywords\"}]} " +
                        "(No other properties, no markdown).\n" +
                        "Every item MUST have a non-empty Korean 'reason' explaining the match and mentioning at least one provided keyword.\n" +
                        "Prioritize titles semantically related to keywords. If ties, prefer more informative titles.";

        String userPrompt = String.format(Locale.ROOT,
                "Keywords: %s\n\n" +
                        "Catalog (JSON lines, up to 100):\n%s\n\n" +
                        "Task:\n" +
                        "- Select top %d items best matching the keywords.\n" +
                        "- Output ONLY JSON with this schema:\n" +
                        "{\n" +
                        "  \"items\": [\n" +
                        "    {\"id\": 123, \"reason\": \"한국어로 간단한 근거(제공된 키워드를 1개 이상 포함)\"}\n" +
                        "  ]\n" +
                        "}\n" +
                        "- NEVER return empty 'reason'.",
                String.valueOf(keywords),
                catalog.toString(),
                limit
        );

        Map<String, Object> body = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        messages.add(Map.of("role", "user", "content", userPrompt));
        body.put("model", model);
        body.put("messages", messages);
        body.put("temperature", 0.3);
        body.put("max_tokens", 800);
        body.put("top_p", 1);
        body.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            String requestJson = MAPPER.writeValueAsString(body);
            String raw = restTemplate.exchange(
                    "https://api.groq.com/openai/v1/chat/completions",
                    HttpMethod.POST, new HttpEntity<>(requestJson, headers), String.class
            ).getBody();

            String content = extractAssistantContent(raw);
            JsonNode root = MAPPER.readTree(content);
            JsonNode items = root.path("items");
            if (!items.isArray()) throw new IllegalStateException("No items array");

            Map<Long, LocalArtEvent> index = events.stream()
                    .collect(Collectors.toMap(LocalArtEvent::getId, x -> x));

            List<RecommendedEventDto> out = new ArrayList<>();
            for (JsonNode it : items) {
                long id = it.path("id").asLong(-1);
                String reason = it.path("reason").asText("");

                LocalArtEvent e = index.get(id);
                if (e == null) continue;

                if (reason == null || reason.isBlank()) {
                    reason = buildLocalReason(e.getTitle(), keywords);
                }

                out.add(RecommendedEventDto.builder()
                        .id(e.getId())
                        .title(e.getTitle())
                        .url(e.getUrl())
                        .mainCategory(e.getMainCategory())
                        .imageUrl(e.getImageUrl())
                        .matchedKeywords(reason)
                        .build());
            }

            if (out.size() > limit) out = out.subList(0, limit);
            return RecommendEventsResponse.builder().items(out).build();

        } catch (Exception ex) {
            log.warn("Groq recommend failed, fallback to local heuristic. cause={}", ex.toString());
            return fallbackRecommend(keywords, events, limit);
        }
    }

    /* -------- 폴백: 키워드 포함수 기반, matchedKeywords 채우기 -------- */
    private RecommendEventsResponse fallbackRecommend(List<String> keywords, List<LocalArtEvent> events, int limit) {
        List<String> normKws = (keywords == null ? List.<String>of() : keywords).stream()
                .filter(Objects::nonNull).map(this::normalizeKo).filter(s -> !s.isBlank()).toList();

        record Scored(LocalArtEvent e, double score, String reason) {}

        List<Scored> scored = events.stream().map(e -> {
            String title = Optional.ofNullable(e.getTitle()).orElse("");
            String nt = normalizeKo(title);
            double score = 0.0;
            List<String> matched = new ArrayList<>();
            for (String k : normKws) {
                if (!k.isBlank() && nt.contains(k)) {
                    score += 3.0;
                    matched.add(k);
                }
            }
            String reason = matched.isEmpty() ? "키워드와의 직접 매칭이 적지만 제목이 관련 있어 보입니다."
                    : "매칭 키워드: " + String.join(", ", matched);
            return new Scored(e, score, reason);
        }).sorted(
                Comparator.<Scored, Double>comparing(s -> s.score).reversed()
                        .thenComparing(s -> s.e.getId(), Comparator.nullsLast(Comparator.reverseOrder()))
        ).limit(limit).toList();

        List<RecommendedEventDto> list = scored.stream().map(s -> RecommendedEventDto.builder()
                .id(s.e.getId())
                .title(s.e.getTitle())
                .url(s.e.getUrl())
                .mainCategory(s.e.getMainCategory())
                .imageUrl(s.e.getImageUrl())
                .matchedKeywords(s.reason)
                .build()).toList();

        return RecommendEventsResponse.builder().items(list).build();
    }

    /* -------------------- 유틸 -------------------- */
    private boolean nz(String s){ return s != null && !s.isBlank(); }

    private String normalizeKo(String s) {
        if (s == null) return "";
        String t = Normalizer.normalize(s, Normalizer.Form.NFKC).toLowerCase(Locale.ROOT);
        t = t.replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}\\p{IsHan}\\p{Blank}]", " ");
        return t.replaceAll("\\s+", " ").trim();
    }

    private String jsonEscape(String s){
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"","\\\"").replace("\n"," ").replace("\r"," ");
    }

    private String buildLocalReason(String title, List<String> keywords) {
        String nt = normalizeKo(Optional.ofNullable(title).orElse(""));
        List<String> normKws = (keywords == null ? List.<String>of() : keywords).stream()
                .filter(Objects::nonNull).map(this::normalizeKo).filter(s -> !s.isBlank()).toList();
        List<String> matched = normKws.stream().filter(nt::contains).toList();
        return matched.isEmpty()
                ? "제공된 키워드와 직접 매칭이 적지만 관련성이 있어 보입니다."
                : "매칭 키워드: " + String.join(", ", matched);
    }
}
