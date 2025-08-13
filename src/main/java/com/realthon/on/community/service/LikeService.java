package com.realthon.on.community.service;

import com.realthon.on.community.entity.Board;
import com.realthon.on.community.entity.Like;
import com.realthon.on.community.repository.BoardRepository;
import com.realthon.on.community.repository.LikeRepository;
import com.realthon.on.global.base.response.exception.BusinessException;
import com.realthon.on.global.base.response.exception.ExceptionType;
import com.realthon.on.user.entity.User;
import com.realthon.on.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    //좋아요 추가
    public void addLike(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ExceptionType.BOARD_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        boolean exists = likeRepository.existsByBoardAndUser(board, user);
        if (exists) {
            throw new BusinessException(ExceptionType.LIKE_ALREADY_EXISTS);
        }

        Like like = Like.builder()
                .board(board)
                .user(user)
                .build();

        likeRepository.save(like);
    }

    // 좋아요 취소
    public void removeLike(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ExceptionType.BOARD_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        Like like = likeRepository.findByBoardAndUser(board, user)
                .orElseThrow(() -> new BusinessException(ExceptionType.LIKE_NOT_FOUND));

        likeRepository.delete(like);
    }

    // 게시글 좋아요 개수 조회
    @Transactional(readOnly = true)
    public long getLikesCountByBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ExceptionType.BOARD_NOT_FOUND));
        return likeRepository.countByBoard(board);
    }
}
