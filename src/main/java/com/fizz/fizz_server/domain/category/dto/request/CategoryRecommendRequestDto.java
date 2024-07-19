package com.fizz.fizz_server.domain.category.dto.request;

import com.fizz.fizz_server.domain.category.domain.CategoryRecommendation;
import com.fizz.fizz_server.domain.user.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CategoryRecommendRequestDto {

    @NotNull(message = "title은 빈값일 수 없습니다.")
    private String title;

    @NotNull(message = "description은 빈값일 수 없습니다.")
    private String description;

    public CategoryRecommendation toEntity(User user){
        return CategoryRecommendation.builder().
                title(this.title).
                description(this.description).
                user(user).build();
    }

}
