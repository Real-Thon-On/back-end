package com.realthon.on.ai.groq.controller;

import com.realthon.on.ai.groq.dto.request.DiaryAnalyzeRequest;
import com.realthon.on.ai.groq.dto.request.EvaluateHarmfulnessRequest;
import com.realthon.on.ai.groq.dto.request.RecommendEventsRequest;
import com.realthon.on.ai.groq.dto.response.DiaryAnalyzeResponse;
import com.realthon.on.ai.groq.dto.response.EvaluateHarmfulnessResponse;
import com.realthon.on.ai.groq.dto.response.RecommendEventsResponse;
import com.realthon.on.ai.groq.service.GroqApiService;
import com.realthon.on.ai.groq.store.DiaryAnalyzeTempStore;
import com.realthon.on.global.base.response.ResponseBody;
import com.realthon.on.global.base.response.ResponseUtil;
import com.realthon.on.global.base.response.exception.BusinessException;
import com.realthon.on.global.base.response.exception.ExceptionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "AI (Groq) API", description = "Groq 기반 일기 분석/유해성 평가 API")
@RestController
@RequestMapping(
        value = "/api/ai",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class GroqApiController {

    @Autowired
    private GroqApiService groqApiService;

    @Autowired
    private DiaryAnalyzeTempStore diaryAnalyzeTempStore;

    @Operation(
            summary = "문장 유해성 평가",
            description = "입력 텍스트의 유해성을 0~10 점수로 평가합니다. 모델 출력만 숫자로 정규화하여 반환합니다."
    )
    @PostMapping("/harmfulness")
    public ResponseEntity<ResponseBody<EvaluateHarmfulnessResponse>> evaluateHarmfulness(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "유해성 평가 대상 문장",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = EvaluateHarmfulnessRequest.class),
                            examples = @ExampleObject(
                                    name = "harmfulness-request",
                                    value = "{\n  \"sentence\": \"I hate you.\"\n}"
                            )
                    )
            )
            @RequestBody EvaluateHarmfulnessRequest request
    ) throws IOException {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(groqApiService.evaluateHarmfulness(request)));
    }
    @Operation(
            summary = "일기 분석 및 공감 답변 생성",
            description = "한국어 일기를 분석하여 감정/시각화 지표(JSON)와 공감/격려 답변 텍스트를 생성합니다. 프롬프트 지침은 영어, 출력은 한국어입니다."
    )
    @PostMapping("/diary/analyze")
    public ResponseEntity<ResponseBody<DiaryAnalyzeResponse>> analyzeDiary(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "일기 분석 요청 본문 (todayDate / userDiaryText)",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = DiaryAnalyzeRequest.class),
                            examples = @ExampleObject(
                                    name = "diary-analyze-request",
                                    value = "{\n" +
                                            "  \"todayDate\": \"2025-08-17\",\n" +
                                            "  \"userDiaryText\": \"오늘은 일을 해도 진척이 없어서 스스로가 부족하게 느껴졌습니다. 팀원들 눈치도 보이고 내일도 같은 하루일까 걱정이 됩니다.\"\n" +
                                            "}"
                            )
                    )
            )
            @RequestBody DiaryAnalyzeRequest request
    ) throws IOException {
        DiaryAnalyzeResponse result = groqApiService.analyzeDiary(request);
        diaryAnalyzeTempStore.save(result);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(result));
    }

    @GetMapping("/diary/analyze/result")
    public ResponseEntity<ResponseBody<DiaryAnalyzeResponse>> getLastDiaryAnalysis() {
        DiaryAnalyzeResponse last = diaryAnalyzeTempStore.getLastResult();
        if (last == null) {
            throw new BusinessException(ExceptionType.RESULT_NOT_FOUNT);
        }
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(last));
    }


    @Operation(
            summary = "키워드 기반 지역 예술 행사 추천",
            description = "일기 분석에서 얻은 핵심 키워드만 전달하면, 종료일 임박 Top100 카탈로그와 매칭해 Groq로 상위 N개 추천을 반환합니다."
    )
    @PostMapping("/diary/recommend")
    public ResponseEntity<ResponseBody<RecommendEventsResponse>> recommend(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "추천 요청 본문 (keywords / limit)",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = com.realthon.on.ai.groq.dto.request.RecommendEventsRequest.class),
                            examples = @ExampleObject(
                                    name = "recommend-request",
                                    value = "{\n" +
                                            "  \"keywords\": [\"업무 피로\", \"자기효능감 저하\", \"팀원들\", \"부족한 자신\"],\n" +
                                            "  \"limit\": 5\n" +
                                            "}"
                            )
                    )
            )
            @RequestBody com.realthon.on.ai.groq.dto.request.RecommendEventsRequest request
    ) {
        var res = groqApiService.recommendEvents(request);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(res));
    }

}
