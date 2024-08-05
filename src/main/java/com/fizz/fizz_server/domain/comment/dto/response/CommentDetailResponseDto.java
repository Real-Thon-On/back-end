package com.fizz.fizz_server.domain.comment.dto.response;

import com.fizz.fizz_server.domain.comment.domain.Comment;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Setter
@NoArgsConstructor
@Getter
public class CommentDetailResponseDto {

    private Long commentId;
    private Long parentId;
    private Long userId;
    private String content;
    private Integer likeCount;
    private Long childCount;
    private String nickname;
    private LocalDateTime createdAt;
    private String profileImage;
    private String profileId;


    public CommentDetailResponseDto(Long commentId, Long parentId, Long userId, String content, Integer likeCount, Long childCount, String nickname, LocalDateTime createdAt, String profileImage, String profileId) {
        this.commentId = commentId;
        this.parentId = parentId;
        this.userId = userId;
        this.content = content;
        this.likeCount = likeCount;
        this.childCount = childCount;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.profileImage = profileImage;
        this.profileId = profileId;
    }


    public static CommentDetailResponseDto toDTO(Comment comment, Long childCount ){
        CommentDetailResponseDto dto = new CommentDetailResponseDto();
        dto.commentId = comment.getId();
        if(comment.getParent() != null ) dto.parentId = comment.getParent().getId();
        dto.userId = comment.getUser().getId();
        dto.content = comment.getContent();
        dto.likeCount = comment.getCommentLikes().size();
        dto.childCount = childCount;
        dto.nickname = comment.getUser().getNickname();
        dto.createdAt = comment.getCreatedAt();
        dto.profileImage=comment.getUser().getProfileImage();
        dto.profileId=comment.getUser().getProfileId();
        return dto;
    }
}
