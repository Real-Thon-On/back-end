package com.fizz.fizz_server.domain.comment.dto.response;

import com.fizz.fizz_server.domain.comment.domain.Comment;
import lombok.*;

@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentInfoResponseDto {

    private Long commentId;
    private Long parentId;
    private Long userId;
    private String content;
    private Integer likeCount;

    public static CommentInfoResponseDto toDTO(Comment comment){
        CommentInfoResponseDto dto = new CommentInfoResponseDto();
        dto.commentId = comment.getId();
        if(comment.getParent() != null ) dto.parentId = comment.getParent().getId();
        dto.userId = comment.getUser().getId();
        dto.content = comment.getContent();
        dto.likeCount = comment.getCommentLikes().size();
        return dto;
    }
}
