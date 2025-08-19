package com.realthon.on.community.service;

import com.realthon.on.community.dto.request.CommunityReqeustDto;
import com.realthon.on.community.dto.response.CommunityResponseDto;
import com.realthon.on.community.entity.Board;
import com.realthon.on.community.entity.HashTagType;
import com.realthon.on.community.repository.BoardRepository;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    //게시글 생성
    @Transactional
    public CommunityResponseDto.BoardResponseDto createBoard(CommunityReqeustDto.AddBaordRequestDto request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->  new BusinessException(ExceptionType.USER_NOT_FOUND));

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .hashtags(request.getHashtags())
                .build();

        boardRepository.save(board);
        return CommunityResponseDto.fromBoardEntity(board);
    }

    //해시태그별 게시글 목록 조회
    public List<CommunityResponseDto.BoardResponseDto> getBoardsByHashTag(HashTagType hashtag) {
        List<Board> boards = boardRepository.findDistinctByHashtags(hashtag);
        return boards.stream()
                .map(CommunityResponseDto::fromBoardEntity)
                .collect(Collectors.toList());
    }

    //게시글 수정
    @Transactional
    public CommunityResponseDto.BoardResponseDto updateBoard(Long id, CommunityReqeustDto.UpdateBoardRequestDto requestDto, Long authenticatedUserId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionType.BOARD_NOT_FOUND));

        if (!board.getUser().getId().equals(authenticatedUserId)) {
            throw new BusinessException(ExceptionType.ACCESS_DENIED);
        }

        board.update(requestDto.getTitle(), requestDto.getContent(),requestDto.getHashtags());
        return CommunityResponseDto.fromBoardEntity(board);
    }

    @Transactional
    public void deleteBoard(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new BusinessException(ExceptionType.BOARD_NOT_FOUND);
        }
        boardRepository.deleteById(id);
    }
}
