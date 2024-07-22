package com.fizz.fizz_server.domain.challenge.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fizz.fizz_server.domain.category.domain.Category;
import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import com.fizz.fizz_server.domain.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateChallengeRequestDto {

    @PositiveOrZero(message = "categoryId는 음수값을 허용하지 않습니다.")
    @NotNull(message = "categoryId는 빈값일 수 없습니다.")
    private Long categoryId;

    @NotBlank(message = "title은 빈값일 수 없습니다.")
    private String title;

    @NotBlank(message = "description은 빈값일 수 없습니다.")
    private String description;

    @JsonProperty("isActive")
    private boolean isActive;


    public Challenge toChallenge(User creator, Category category){
        return Challenge.builder()
                .creator(creator)
                .category(category)
                .description(this.description)
                .isActive(this.isActive)
                .title(this.title)
                .build();
    }

}
