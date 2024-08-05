package com.fizz.fizz_server.domain.recommend.api;

import com.fizz.fizz_server.domain.post.dto.response.PostInfo;
import com.fizz.fizz_server.domain.recommend.service.RecommendService;
import com.fizz.fizz_server.domain.user.domain.CustomUserPrincipal;
import com.fizz.fizz_server.global.base.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.fizz.fizz_server.global.base.response.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/recommend")
public class RecommendController {
    private final RecommendService recommendService;

    // 추천 시스템의 성능이 느리기 때문에 그 동안 보여줄 게시물
    @PostMapping("/pre-post")
    public ResponseEntity<ResponseBody<Page<PostInfo>>> preRecommendPost(@AuthenticationPrincipal CustomUserPrincipal principal,
                                                                     Pageable pageable) {
        Page<PostInfo> response = recommendService.preRecommendPost(principal.getUserId(), pageable);
        return ResponseEntity.ok(createSuccessResponse(response));
    }

    // 실제 사용자 맞춤 추천 게시물
    @PostMapping("/post")
    public ResponseEntity<ResponseBody<List<PostInfo>>> recommendPost(@AuthenticationPrincipal CustomUserPrincipal principal) {
        List<PostInfo> response = recommendService.recommendPost(principal.getUserId());
        return ResponseEntity.ok(createSuccessResponse(response));
    }
}
