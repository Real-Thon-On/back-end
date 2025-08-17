package com.realthon.on.ai.groq.controller;


import com.realthon.on.ai.groq.dto.request.DiaryAnalyzeRequest;
import com.realthon.on.ai.groq.dto.request.EvaluateHarmfulnessRequest;
import com.realthon.on.ai.groq.dto.response.DiaryAnalyzeResponse;
import com.realthon.on.ai.groq.dto.response.EvaluateHarmfulnessResponse;
import com.realthon.on.ai.groq.service.GroqApiService;
import com.realthon.on.global.base.response.ResponseBody;
import com.realthon.on.global.base.response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(
        value = "/api/ai",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class GroqApiController {

    @Autowired
    private GroqApiService groqApiService;


    @PostMapping("/harmfulness")
    public ResponseEntity<ResponseBody<EvaluateHarmfulnessResponse>> evaluateHarmfulness(@RequestBody  EvaluateHarmfulnessRequest request) throws IOException {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(groqApiService.evaluateHarmfulness(request)));
    }

    @PostMapping("/diary/analyze")
    public ResponseEntity<ResponseBody<DiaryAnalyzeResponse>> analyzeDiary(@RequestBody DiaryAnalyzeRequest request) throws IOException {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(groqApiService.analyzeDiary(request)));
    }



}