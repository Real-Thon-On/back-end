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
        private String userName;
        private String profileImageUrl;
        private Set<HashTagType> hashtags;
        private List<String> imageUrls;
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
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .userName(board.getUser().getUsername())
                .profileImageUrl(board.getUser().getProfileImageUrl())
                .imageUrls(board.getImageUrls())
                .build();
}

    @Getter
    @Builder
    public static class CommentResponseDto {
        private Long commentId;
        private String content;
        private Long boardId;
        private Long userId;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

    }

    public static CommentResponseDto fromCommentEntity(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .userId(comment.getUser().getId())
                .boardId(comment.getBoard().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class CountResponseDto {
        private Long boardId;
        private Long count;
    }

}
