package com.realthon.on.community.controller;

import com.realthon.on.community.service.LikeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.realthon.on.community.dto.response.CommunityResponseDto;
import com.realthon.on.global.base.response.ResponseBody;
import com.realthon.on.global.base.response.ResponseUtil;
import com.realthon.on.security.oauth2.principal.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "LIKE API", description = "게시글 좋아요 API")
@RestController
@RequestMapping("/api/boards/{boardId}/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @Operation(summary = "좋아요 등록", description = "새로운 좋아요를 등록합니다.")
    @PostMapping
    public ResponseEntity<ResponseBody<String>> like(@PathVariable Long boardId,
                                     @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likeService.addLike(boardId, principalDetails.getUser().getId());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse("좋아요가 성공적으로 등록되었습니다."));
    }

    @Operation(summary = "좋아요 취소", description = "좋아요를 취소합니다.")
    @DeleteMapping
    public ResponseEntity<ResponseBody<String>> unlike(@PathVariable Long boardId,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likeService.removeLike(boardId, principalDetails.getId());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse("좋아요가 성공적으로 취소되었습니다."));
    }

    @Operation(summary = "좋아요 개수", description = "게시글별 좋아요 개수를 집계합니다")
    @GetMapping("/count")
    public ResponseEntity<ResponseBody<CommunityResponseDto.LikesCountResponseDto>> getLikesCount(@PathVariable Long boardId) {
        long count = likeService.getLikesCountByBoard(boardId);
        CommunityResponseDto.LikesCountResponseDto responseDto = new CommunityResponseDto.LikesCountResponseDto(boardId, count);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }
}
