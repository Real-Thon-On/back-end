package com.fizz.fizz_server.domain.category.dto.response;
import com.fizz.fizz_server.domain.category.domain.Category;
import lombok.*;

@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CategoryInfoResponseDto {
    private Long categoryId;
    private String title;

    public static CategoryInfoResponseDto toDTO(Category category){
        CategoryInfoResponseDto dto = new CategoryInfoResponseDto();
        dto.categoryId =category.getCategoryId();
        dto.title =category.getTitle();
        return dto;
    }

}
