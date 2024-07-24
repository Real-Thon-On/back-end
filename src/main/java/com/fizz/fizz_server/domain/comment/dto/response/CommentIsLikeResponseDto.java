package com.fizz.fizz_server.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentIsLikeResponseDto {
    @JsonProperty("isLike")
    private boolean isLike;
}
