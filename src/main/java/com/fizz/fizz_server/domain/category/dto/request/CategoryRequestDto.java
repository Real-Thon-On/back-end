package com.fizz.fizz_server.domain.category.dto.request;

import com.fizz.fizz_server.domain.category.domain.Category;
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
public class CategoryRequestDto {
    @NotNull(message = "title은 빈값일 수 없습니다.")
    private String title;

    public Category toEntity(){
        return Category.builder().
                title(this.title).build();
    }

}
