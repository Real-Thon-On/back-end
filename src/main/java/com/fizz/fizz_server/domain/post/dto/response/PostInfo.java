package com.fizz.fizz_server.domain.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fizz.fizz_server.domain.post.domain.Post;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostInfo(
        Long id,
        String title,
        String content,
        List<String> fileUrls,
        int commentCount,
        int likeCount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        UserInfo userInfo
) {
    public record UserInfo(
            Long id,
            String nickname,
            String profileImage
    ) {
    }

    public static PostInfo from(Post post) {
        return PostInfo.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .fileUrls(post.getFileUrls())
                .commentCount(post.getComments().size())
                .likeCount(post.getPostLikes().size())
                .createdAt(post.getCreatedAt())
                .userInfo(new UserInfo(
                        post.getUser().getId(),
                        post.getUser().getNickname(),
                        post.getUser().getProfileImage()
                ))
                .build();
    }
}
