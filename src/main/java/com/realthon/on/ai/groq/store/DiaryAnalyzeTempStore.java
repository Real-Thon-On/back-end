package com.realthon.on.ai.groq.store;

import com.realthon.on.ai.groq.dto.response.DiaryAnalyzeResponse;
import org.springframework.stereotype.Service;

@Service
public class DiaryAnalyzeTempStore {

    private DiaryAnalyzeResponse lastResult; // 서버 메모리에만 저장됨

    public void save(DiaryAnalyzeResponse response) {
        this.lastResult = response;
    }

    public DiaryAnalyzeResponse getLastResult() {
        return lastResult;
    }
}
