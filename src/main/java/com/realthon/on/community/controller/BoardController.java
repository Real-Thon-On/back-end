package com.realthon.on.community.controller;

import com.realthon.on.community.dto.request.CommunityReqeustDto;
import com.realthon.on.community.dto.response.CommunityResponseDto;
import com.realthon.on.community.entity.HashTagType;
import com.realthon.on.community.service.BoardService;
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

@Tag(name = "BOARD API", description = "게시글 API")
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @PostMapping
    public ResponseEntity<ResponseBody<CommunityResponseDto.BoardResponseDto>> createDiary(@RequestBody @Valid CommunityReqeustDto.AddBaordRequestDto requestDto) {
        CommunityResponseDto.BoardResponseDto responseDto = boardService.createBoard(requestDto);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @Operation(summary = "해시태그별 게시글 목록 조회", description = "특정 해시태그에 해당하는 게시글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ResponseBody<List<CommunityResponseDto.BoardResponseDto>>> getBoardsByHashtag(
            @RequestParam HashTagType hashtag) {
        List<CommunityResponseDto.BoardResponseDto> boards = boardService.getBoardsByHashTag(hashtag);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(boards));
    }

    @Operation(summary = "게시글 수정", description = "게시글 ID에 해당하는 게시글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody<CommunityResponseDto.BoardResponseDto>> updateBoard(
            @PathVariable Long id,
            @RequestBody @Valid CommunityReqeustDto.UpdateBoardRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        CommunityResponseDto.BoardResponseDto responseDto = boardService.updateBoard(id, requestDto,principalDetails.getId());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @Operation(summary = "게시글 삭제", description = "게시글 ID에 해당하는 게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody<String>> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse("게시글이 성공적으로 삭제되었습니다."));
    }
}
