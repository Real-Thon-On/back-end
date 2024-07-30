package com.fizz.fizz_server.domain.category.service;

import com.fizz.fizz_server.domain.category.dto.request.CategoryRecommendRequestDto;
import com.fizz.fizz_server.domain.category.dto.request.CategoryRequestDto;
import com.fizz.fizz_server.domain.category.dto.response.CategoryInfoResponseDto;
import com.fizz.fizz_server.domain.user.domain.User;

import java.util.List;


public interface CategoryService {
    public List<CategoryInfoResponseDto> getAllCategories();

    public void createCategoryRecommend(Long UserId , CategoryRecommendRequestDto requestDto);

    public void createCategory(CategoryRequestDto requestDto);

    public void deleteCategory(Long categoryId);
}
