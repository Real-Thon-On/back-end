package com.fizz.fizz_server.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateChildCommentRequestDto {
    @NotNull(message = "parentCommentId는 빈값일 수 없습니다.")
    private Long parentCommentId;

    @NotBlank(message = "content은 빈값일 수 없습니다.")
    private String content;
}
