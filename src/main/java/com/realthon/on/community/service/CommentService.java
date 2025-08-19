package com.realthon.on.community.service;

import com.realthon.on.community.dto.request.CommunityReqeustDto;
import com.realthon.on.community.dto.response.CommunityResponseDto;
import com.realthon.on.community.entity.Board;
import com.realthon.on.community.entity.Comment;
import com.realthon.on.community.repository.BoardRepository;
import com.realthon.on.community.repository.CommentRepository;
import com.realthon.on.global.base.response.exception.BusinessException;
import com.realthon.on.global.base.response.exception.ExceptionType;
import com.realthon.on.user.entity.User;
import com.realthon.on.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    //댓글 생성
    @Transactional
    public CommunityResponseDto.CommentResponseDto createComment(Long boardId, CommunityReqeustDto.AddCommentRequestDto request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->  new BusinessException(ExceptionType.USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() ->  new BusinessException(ExceptionType.BOARD_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .board(board)
                .build();

        commentRepository.save(comment);
        return CommunityResponseDto.fromCommentEntity(comment);
    }

    //게시글 별 댓글 목록 조회
    public List<CommunityResponseDto.CommentResponseDto> getCommentsByBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ExceptionType.BOARD_NOT_FOUND));
        List<Comment> comments = commentRepository.findAllByBoard(board);

        return comments.stream()
                .map(CommunityResponseDto::fromCommentEntity)
                .collect(Collectors.toList());
    }

    //댓글 수정
    @Transactional
    public CommunityResponseDto.CommentResponseDto updateComment(Long id, CommunityReqeustDto.UpdateCommentRequestDto requestDto, Long authenticatedUserId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionType.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(authenticatedUserId)) {
            throw new BusinessException(ExceptionType.ACCESS_DENIED);
        }

        comment.update(requestDto.getContent());
        return CommunityResponseDto.fromCommentEntity(comment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new BusinessException(ExceptionType.COMMENT_NOT_FOUND);
        }
        commentRepository.deleteById(id);
    }
}
