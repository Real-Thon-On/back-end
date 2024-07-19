package com.fizz.fizz_server.domain.category.service;

import com.fizz.fizz_server.domain.category.domain.Category;
import com.fizz.fizz_server.domain.category.domain.CategoryRecommendation;
import com.fizz.fizz_server.domain.category.dto.request.CategoryRecommendRequestDto;
import com.fizz.fizz_server.domain.category.dto.request.CategoryRequestDto;
import com.fizz.fizz_server.domain.category.repository.CategoryRecommendationRepository;
import com.fizz.fizz_server.domain.category.repository.CategoryRepository;
import com.fizz.fizz_server.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryRecommendationRepository categoryRecommendationRepository;


    @Transactional
    @Override
    public void createCategoryRecommend(User user, CategoryRecommendRequestDto requestDto) {
        CategoryRecommendation categoryRecommendation = requestDto.toEntity(user);
        CategoryRecommendation savedCategoryRecommendation = categoryRecommendationRepository.save(categoryRecommendation);
        log.info(savedCategoryRecommendation.toString());

    }

    @Transactional
    @Override
    public void createCategory(CategoryRequestDto requestDto) {
        Category category= requestDto.toEntity();
        Category savedCategory = categoryRepository.save(category);
        log.info(savedCategory.toString());
    }

    @Transactional
    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
