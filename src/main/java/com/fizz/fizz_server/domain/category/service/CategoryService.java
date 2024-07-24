package com.fizz.fizz_server.domain.category.service;

import com.fizz.fizz_server.domain.category.dto.request.CategoryRecommendRequestDto;
import com.fizz.fizz_server.domain.category.dto.request.CategoryRequestDto;
import com.fizz.fizz_server.domain.user.domain.User;


public interface CategoryService {
    public void createCategoryRecommend(Long UserId , CategoryRecommendRequestDto requestDto);

    public void createCategory(CategoryRequestDto requestDto);

    public void deleteCategory(Long categoryId);
}
