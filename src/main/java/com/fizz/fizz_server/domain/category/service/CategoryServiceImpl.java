package com.fizz.fizz_server.domain.category.service;

import com.fizz.fizz_server.domain.category.domain.Category;
import com.fizz.fizz_server.domain.category.domain.CategoryRecommendation;
import com.fizz.fizz_server.domain.category.dto.request.CategoryRecommendRequestDto;
import com.fizz.fizz_server.domain.category.dto.request.CategoryRequestDto;
import com.fizz.fizz_server.domain.category.dto.response.CategoryInfoResponseDto;
import com.fizz.fizz_server.domain.category.repository.CategoryRecommendationRepository;
import com.fizz.fizz_server.domain.category.repository.CategoryRepository;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.fizz.fizz_server.global.base.response.exception.ExceptionType.USER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryRecommendationRepository categoryRecommendationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryInfoResponseDto> getAllCategories() {
        List<Category> entityList= categoryRepository.findAll();
        List<CategoryInfoResponseDto> dtoList = entityList.
                        stream().map(entity-> CategoryInfoResponseDto.toDTO(entity)).
                        collect(Collectors.toList());
        return dtoList;
    }

    @Transactional
    @Override
    public void createCategoryRecommend(Long userId, CategoryRecommendRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
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
