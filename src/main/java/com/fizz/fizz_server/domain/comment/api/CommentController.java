package com.fizz.fizz_server.domain.comment.api;


import com.fizz.fizz_server.domain.comment.dto.request.ChangeCommentRequestDto;
import com.fizz.fizz_server.domain.comment.dto.request.CreateChildCommentRequestDto;
import com.fizz.fizz_server.domain.comment.dto.request.CreateParentCommentRequestDto;
import com.fizz.fizz_server.domain.comment.dto.response.CommentInfoResponseDto;
import com.fizz.fizz_server.domain.comment.dto.response.CommentIsLikeResponseDto;
import com.fizz.fizz_server.domain.comment.service.CommentService;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    // UserPrincipal 추후 반영시 삭제
    private final UserRepository userRepository;

    // 댓글 작성
    @PostMapping("/post/{postId}")
    public ResponseEntity<ResponseBody> createParentCommentByPostId(@PathVariable Long postId, @Valid @RequestBody CreateParentCommentRequestDto requestDto){// parameter 에 @AuthenticationPrincipal UserPrincipal user 추후 추가
        User tempUser = userRepository.findById(1L).get(); // UserPrincipal 추후 추가되면 삭제.
        commentService.createParentCommentByPostId(postId,requestDto, tempUser);
        return ResponseEntity.status(HttpStatus.CREATED) .body(ResponseUtil.createSuccessResponse());
    }

    // 대댓글 작성
    @PostMapping("/post/{postId}/reply")
    public ResponseEntity<ResponseBody> createChildCommentByPostId(@PathVariable Long postId, @Valid @RequestBody CreateChildCommentRequestDto requestDto){// parameter 에 @AuthenticationPrincipal UserPrincipal user 추후 추가
        User tempUser = userRepository.findById(1L).get(); // UserPrincipal 추후 추가되면 삭제.
        commentService.createChildCommentByPostId(postId,requestDto, tempUser);
        return ResponseEntity.status(HttpStatus.CREATED) .body(ResponseUtil.createSuccessResponse());
    }

    // 해당 게시글의 모든 부모 댓글 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponseBody<List<CommentInfoResponseDto>>> getAllParentCommentsByPostId(@PathVariable Long postId){
        List<CommentInfoResponseDto> responseDtos= commentService.getAllParentCommentsByPostId(postId);
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse(responseDtos));
    }

    // 해당 댓글의 모든 대댓글 조회
    @GetMapping("/post/{commentId}/reply")
    public ResponseEntity<ResponseBody<List<CommentInfoResponseDto>>> getAllChildCommentsByCommentId(@PathVariable Long commentId){
        List<CommentInfoResponseDto> responseDtos= commentService.getAllChildCommentsByCommentId(commentId);
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse(responseDtos));
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseBody> changeCommentByCommentId(@PathVariable Long commentId, @Valid @RequestBody ChangeCommentRequestDto requestDto){


        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse());
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseBody> deleteCommentByCommentId(@PathVariable Long commentId){

        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse());
    }

    // 댓글 좋아요 여부 확인
    @GetMapping("/{commentId}/like")
    public ResponseEntity<ResponseBody<CommentIsLikeResponseDto>> getIsLikedByCommentId(@PathVariable Long commentId){ // parameter 에 @AuthenticationPrincipal UserPrincipal user 추후 추가

        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse(null));
    }

    // 댓글 좋아요 생성
    @PostMapping("/{commentId}/like")
    public ResponseEntity<ResponseBody> createLikeByCommentId(@PathVariable Long commentId){ // parameter 에 @AuthenticationPrincipal UserPrincipal user 추후 추가

        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse());
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<ResponseBody> deleteLikeByCommentId(@PathVariable Long commentId){ // parameter 에 @AuthenticationPrincipal UserPrincipal user 추후 추가

        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse());
    }
}
