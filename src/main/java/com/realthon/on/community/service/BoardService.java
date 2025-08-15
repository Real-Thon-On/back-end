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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    //게시글 생성
    @Transactional
    public CommunityResponseDto.BoardResponseDto createBoard(CommunityReqeustDto.AddBaordRequestDto request, List<MultipartFile> images) {

        List<String> imageUrls = new ArrayList<>();

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->  new BusinessException(ExceptionType.USER_NOT_FOUND));


        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                        Path uploadDir = Paths.get("uploads");
                        Files.createDirectories(uploadDir);
                        Path uploadPath = uploadDir.resolve(filename);
                        Files.write(uploadPath, image.getBytes());
                        imageUrls.add("/uploads/" + filename);
                    } catch (IOException e) {
                        log.error("이미지 업로드 실패", e);
                        throw new BusinessException(ExceptionType.IMAGE_UPLOAD_FAILED);
                    }
                }
            }
        }

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .hashtags(request.getHashtags())
                .imageUrls(imageUrls)
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
    public CommunityResponseDto.BoardResponseDto updateBoard(Long id,
                                                             CommunityReqeustDto.UpdateBoardRequestDto requestDto,
                                                             List<MultipartFile> images,
                                                             Long authenticatedUserId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionType.BOARD_NOT_FOUND));

        if (!board.getUser().getId().equals(authenticatedUserId)) {
            throw new BusinessException(ExceptionType.ACCESS_DENIED);
        }

        board.update(requestDto.getTitle(), requestDto.getContent(),requestDto.getHashtags());

        List<String> imageUrls = new ArrayList<>();
        Path uploadDir = Paths.get("uploads");

        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        Files.createDirectories(uploadDir); // 폴더 없으면 생성
                        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                        Path uploadPath = uploadDir.resolve(filename);
                        Files.write(uploadPath, image.getBytes());
                        imageUrls.add("/uploads/" + filename);
                    } catch (IOException e) {
                        log.error("이미지 업로드 실패", e);
                        throw new BusinessException(ExceptionType.IMAGE_UPLOAD_FAILED);
                    }
                }
            }

            // 기존 이미지 삭제
            if (board.getImageUrls() != null) {
                for (String oldUrl : board.getImageUrls()) {
                    try {
                        Path oldImagePath = uploadDir.resolve(Paths.get(oldUrl).getFileName());
                        Files.deleteIfExists(oldImagePath);
                    } catch (IOException e) {
                        log.warn("기존 이미지 삭제 실패: " + oldUrl, e);
                        throw new BusinessException(ExceptionType.IMAGE_UPLOAD_FAILED);
                    }
                }
            }

            board.setImageUrls(imageUrls);
        }

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
