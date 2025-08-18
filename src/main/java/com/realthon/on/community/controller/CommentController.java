package com.realthon.on.community.controller;

import com.realthon.on.community.dto.request.CommunityReqeustDto;
import com.realthon.on.community.dto.response.CommunityResponseDto;
import com.realthon.on.community.service.CommentService;
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

@Tag(name = "COMMENT API", description = "게시글 댓글 API")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "새로운 댓글을 생성합니다.")
    @PostMapping("/api/boards/{boardId}/comments")
    public ResponseEntity<ResponseBody<CommunityResponseDto.CommentResponseDto>> createComment( @PathVariable Long boardId, @RequestBody @Valid CommunityReqeustDto.AddCommentRequestDto requestDto) {
        CommunityResponseDto.CommentResponseDto responseDto = commentService.createComment(boardId,requestDto);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @Operation(summary = "게시글별 댓글 목록 조회", description = "특정 게시글에 해당하는 댓글 목록을 조회합니다.")
    @GetMapping("/api/boards/{boardId}/comments")
    public ResponseEntity<ResponseBody<List<CommunityResponseDto.CommentResponseDto>>> getCommentsByBoard(
            @PathVariable Long boardId) {
        List<CommunityResponseDto.CommentResponseDto> boards = commentService.getCommentsByBoard(boardId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(boards));
    }

    @Operation(summary = "댓글 수정", description = "댓글 ID에 해당하는 댓글을 수정합니다.")
    @PutMapping("/api/comments/{id}")
    public ResponseEntity<ResponseBody<CommunityResponseDto.CommentResponseDto>> updateComment(
            @PathVariable Long id,
            @RequestBody @Valid CommunityReqeustDto.UpdateCommentRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        CommunityResponseDto.CommentResponseDto responseDto = commentService.updateComment(id, requestDto,principalDetails.getId());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 ID에 해당하는 댓글을 삭제합니다.")
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<ResponseBody<String>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse("댓글이 성공적으로 삭제되었습니다."));
    }}
