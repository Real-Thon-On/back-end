package com.realthon.on.psychoTest.controller;

import com.realthon.on.global.base.response.ResponseBody;
import com.realthon.on.global.base.response.ResponseUtil;
import com.realthon.on.psychoTest.dto.request.PsychoTestReqeustDto;
import com.realthon.on.psychoTest.dto.response.PsychoTestResponseDto;
import com.realthon.on.psychoTest.entity.TargetType;
import com.realthon.on.psychoTest.service.PsychoTestService;
import com.realthon.on.security.oauth2.principal.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PSYCHOTEST API", description = "심리검사 API")
@RestController
@RequestMapping("/api/psych-tests")
@RequiredArgsConstructor
public class PsychoTestController {

    private final PsychoTestService psychoTestService;

    @Operation(summary = "타입별 심리 검사 목록 조회", description = "성인용/학생용을 필터해 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ResponseBody<List<PsychoTestResponseDto.TestTypeDto>>> getTests(@RequestParam TargetType type) {
        List<PsychoTestResponseDto.TestTypeDto> testTypes = psychoTestService.getTests(type);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(testTypes));
    }

    @Operation(summary = "특정 검사 문항 조회", description = "특정 검사 상세와 문항 목록을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody<List<PsychoTestResponseDto.QuestionDto>>> getTestDetail(@PathVariable Long id) {
        List<PsychoTestResponseDto.QuestionDto> questions = psychoTestService.getTestDetail(id);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(questions));
    }

    @Operation(summary = "답안 제출 및 검사 결과 조회", description = "제출한 답안 검사 결과를 조회합니다.")
    @PostMapping("/{id}/submit")
    public ResponseEntity<ResponseBody<PsychoTestResponseDto.PsychoResultDto>> submitTest(@PathVariable Long id, @RequestBody PsychoTestReqeustDto.PsychSubmitDto submitDto,
                                  @AuthenticationPrincipal PrincipalDetails principal) {
        PsychoTestResponseDto.PsychoResultDto result=psychoTestService.submitTest(id, principal.getId(), submitDto);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(result));
    }

    @Operation(summary = "모든 검사 결과 조회", description = "모든 답안 검사 결과를 조회합니다.")
    @GetMapping("/results/me")
    public ResponseEntity<ResponseBody<List<PsychoTestResponseDto.PsychoResultDto>>> getMyResults(@AuthenticationPrincipal PrincipalDetails principal) {
        List<PsychoTestResponseDto.PsychoResultDto> results = psychoTestService.getMyResults(principal.getId());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(results));
    }

}

