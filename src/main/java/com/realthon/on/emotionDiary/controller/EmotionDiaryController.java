package com.realthon.on.emotionDiary.controller;

import com.realthon.on.emotionDiary.dto.request.EmotionDiaryRequestDto;
import com.realthon.on.emotionDiary.dto.response.EmotionDiaryResponseDto;
import com.realthon.on.emotionDiary.service.EmotrionDiaryService;
import com.realthon.on.global.base.response.ResponseBody;
import com.realthon.on.global.base.response.ResponseUtil;
import com.realthon.on.security.oauth2.principal.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Diary API", description = "감정일기 CRUD API")
@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class EmotionDiaryController {

    private final EmotrionDiaryService emotionDiaryService;

    @Operation(summary = "감정일기 생성", description = "새로운 감정일기를 생성합니다.")
    @PostMapping
    public ResponseEntity<ResponseBody<EmotionDiaryResponseDto>> createDiary(@RequestBody @Valid EmotionDiaryRequestDto.AddEmotionDiaryRequestDto requestDto) {
        EmotionDiaryResponseDto responseDto = emotionDiaryService.createDiary(requestDto);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }
    @Operation(summary = "특정 감정일기 조회", description = "ID를 통해 특정 감정일기를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody<EmotionDiaryResponseDto>> getDiaryById(@PathVariable Long id) {
        EmotionDiaryResponseDto responseDto = emotionDiaryService.getDiaryById(id);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @Operation(summary = "모든 감정일기 조회", description = "모든 감정일기 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ResponseBody<List<EmotionDiaryResponseDto>>> getAllDiaries() {
        List<EmotionDiaryResponseDto> responses = emotionDiaryService.getAllDiaries();
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responses));
    }

    @Operation(summary = "감정일기 수정", description = "ID를 통해 특정 감정일기를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody<EmotionDiaryResponseDto>> updateDiary(@PathVariable Long id, @RequestBody @Valid EmotionDiaryRequestDto.UpdateEmotionDiaryRequestDto requestDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        EmotionDiaryResponseDto responseDto = emotionDiaryService.updateDiary(id, requestDto,principalDetails.getId() );
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @Operation(summary = "감정일기 삭제", description = "ID를 통해 특정 감정일기를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody<String>> deleteDiary(@PathVariable Long id) {
        emotionDiaryService.deleteDiary(id);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse("일기가 성공적으로 삭제되었습니다."));
    }
}
