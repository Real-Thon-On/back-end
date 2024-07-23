package com.fizz.fizz_server.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentIsLikeResponseDto {
    @JsonProperty("isLike")
    private boolean isLike;
}
