package com.realthon.on.community.dto.response;

import com.realthon.on.community.entity.Board;
import com.realthon.on.community.entity.Comment;
import com.realthon.on.community.entity.HashTagType;
import com.realthon.on.community.entity.Like;
import com.realthon.on.emotionDiary.dto.response.EmotionDiaryResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class CommunityResponseDto {

    @Getter
    @Builder
    public static class BoardResponseDto {
        private Long boardId;
        private String title;
        private String content;
        private Long userId;
        private Set<HashTagType> boardTypes;
        private List<String> hashtags;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

    }
    public static BoardResponseDto fromBoardEntity(Board board) {
        return BoardResponseDto.builder()
                .boardId(board.getId())
                .userId(board.getUser().getId())
                .title(board.getTitle())
                .content(board.getContent())
                .hashtags(board.getHashtags())
                .boardTypes(board.getBoardTypes())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .build();
}

    @Getter
    @Builder
    public static class CommentResponseDto {
        private Long commentId;
        private String content;
        private Long boardId;
        private String userName;
        private String profileImageUrl;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

    }

    public static CommentResponseDto fromCommentEntity(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .userName(comment.getUser().getUsername())
                .profileImageUrl(comment.getUser().getProfileImageUrl())
                .boardId(comment.getBoard().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class LikesCountResponseDto {
        private Long boardId;
        private Long count;
    }

}
