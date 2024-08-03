package com.fizz.fizz_server.domain.comment.service;

import com.fizz.fizz_server.domain.comment.dto.request.ChangeCommentRequestDto;
import com.fizz.fizz_server.domain.comment.dto.request.CreateChildCommentRequestDto;
import com.fizz.fizz_server.domain.comment.dto.request.CreateParentCommentRequestDto;
import com.fizz.fizz_server.domain.comment.dto.response.CommentDetailResponseDto;
import com.fizz.fizz_server.domain.comment.dto.response.CommentIsLikeResponseDto;

import java.util.List;

public interface CommentService {
    // 댓글 작성
    void createParentCommentByPostId(Long postId, CreateParentCommentRequestDto requestDto, Long userId);

    // 대댓글 작성
    void createChildCommentByPostId(Long postId, CreateChildCommentRequestDto requestDto, Long userId);

    // 해당 게시글의 모든 부모 댓글 조회
    List<CommentDetailResponseDto> getAllParentCommentsByPostId(Long postId);

    // 해당 댓글의 모든 대댓글 조회
    List<CommentDetailResponseDto> getAllChildCommentsByCommentId(Long commentId);

    // 댓글 수정
    void changeCommentByCommentId(Long commentId, ChangeCommentRequestDto requestDto);

    // 댓글 삭제
    void deleteCommentByCommentId(Long commentId);

    // 댓글 좋아요 여부 확인
    CommentIsLikeResponseDto getIsLikedByCommentId(Long commentId, Long userId);

    // 댓글 좋아요 생성
    void createLikeByCommentId(Long commentId, Long userId);

    // 댓글 좋아요 취소
    void deleteLikeByCommentId(Long commentId, Long userId);
}
