package com.fizz.fizz_server.domain.comment.api;


import com.fizz.fizz_server.domain.comment.dto.request.ChangeCommentRequestDto;
import com.fizz.fizz_server.domain.comment.dto.request.CreateChildCommentRequestDto;
import com.fizz.fizz_server.domain.comment.dto.request.CreateParentCommentRequestDto;
import com.fizz.fizz_server.domain.comment.dto.response.CommentInfoResponseDto;
import com.fizz.fizz_server.domain.comment.dto.response.CommentInfoWithChildCountResponseDto;
import com.fizz.fizz_server.domain.comment.dto.response.CommentIsLikeResponseDto;
import com.fizz.fizz_server.domain.comment.service.CommentService;
import com.fizz.fizz_server.domain.user.domain.CustomUserPrincipal;
import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/post/{postId}")
    public ResponseEntity<ResponseBody> createParentCommentByPostId(@PathVariable Long postId,
                                                                    @Valid @RequestBody CreateParentCommentRequestDto requestDto,
                                                                    @AuthenticationPrincipal CustomUserPrincipal user){
        commentService.createParentCommentByPostId(postId,requestDto, user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED) .body(ResponseUtil.createSuccessResponse());
    }

    // 대댓글 작성
    @PostMapping("/post/{postId}/reply")
    public ResponseEntity<ResponseBody> createChildCommentByPostId(@PathVariable Long postId,
                                                                   @Valid @RequestBody CreateChildCommentRequestDto requestDto,
                                                                   @AuthenticationPrincipal CustomUserPrincipal user){
        commentService.createChildCommentByPostId(postId,requestDto, user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED) .body(ResponseUtil.createSuccessResponse());
    }

    // 해당 게시글의 모든 부모 댓글 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponseBody<List<CommentInfoWithChildCountResponseDto>>> getAllParentCommentsByPostId(@PathVariable Long postId){
        List<CommentInfoWithChildCountResponseDto> responseDtos= commentService.getAllParentCommentsByPostId(postId);
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse(responseDtos));
    }

    // 해당 댓글의 모든 대댓글 조회
    @GetMapping("/post/{commentId}/reply")
    public ResponseEntity<ResponseBody<List<CommentInfoResponseDto>>> getAllChildCommentsByCommentId(@PathVariable Long commentId){
        List<CommentInfoResponseDto> responseDtos= commentService.getAllChildCommentsByCommentId(commentId);
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse(responseDtos));
    }

    // 댓글 내용 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseBody> changeCommentByCommentId(@PathVariable Long commentId, @Valid @RequestBody ChangeCommentRequestDto requestDto){
        commentService.changeCommentByCommentId(commentId, requestDto);
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse());
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseBody> deleteCommentByCommentId(@PathVariable Long commentId){
        commentService.deleteCommentByCommentId(commentId);
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse());
    }

    // 댓글 좋아요 여부 확인
    @GetMapping("/{commentId}/like")
    public ResponseEntity<ResponseBody<CommentIsLikeResponseDto>> getIsLikedByCommentId(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserPrincipal user){
        CommentIsLikeResponseDto responseDto = commentService.getIsLikedByCommentId(commentId,user.getUserId());
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse(responseDto));
    }

    // 댓글 좋아요 생성
    @PostMapping("/{commentId}/like")
    public ResponseEntity<ResponseBody> createLikeByCommentId(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserPrincipal user){
        commentService.createLikeByCommentId(commentId,user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED) .body(ResponseUtil.createSuccessResponse());
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<ResponseBody> deleteLikeByCommentId(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserPrincipal user) {
        commentService.deleteLikeByCommentId(commentId,user.getUserId());
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse());
    }

}
