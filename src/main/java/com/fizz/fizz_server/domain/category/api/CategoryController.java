package com.fizz.fizz_server.domain.category.api;


import com.fizz.fizz_server.domain.category.dto.request.CategoryRecommendRequestDto;
import com.fizz.fizz_server.domain.category.dto.request.CategoryRequestDto;
import com.fizz.fizz_server.domain.category.dto.response.CategoryInfoResponseDto;
import com.fizz.fizz_server.domain.category.service.CategoryService;
import com.fizz.fizz_server.domain.user.domain.CustomUserPrincipal;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    //모든 카테고리 조회
    @GetMapping()
    public ResponseEntity<ResponseBody<List<CategoryInfoResponseDto>>> getAllCategories(){
        List<CategoryInfoResponseDto> responseDtos = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse( responseDtos ));
    }

    //카테고리 생성 건의
    @PostMapping("/user")
    public ResponseEntity<ResponseBody> createCategoryRecommend(@Valid @RequestBody CategoryRecommendRequestDto requestDto,
                                                                @AuthenticationPrincipal CustomUserPrincipal user) {
        categoryService.createCategoryRecommend( user.getUserId() , requestDto);
        return ResponseEntity.status(HttpStatus.CREATED) .body(ResponseUtil.createSuccessResponse());
    }


    //카테고리 추가
    @PostMapping()
    public ResponseEntity<ResponseBody> createCategory(@Valid @RequestBody CategoryRequestDto requestDto){
        categoryService.createCategory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED) .body(ResponseUtil.createSuccessResponse());
    }


    //카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResponseBody> deleteCategory(@PathVariable Long categoryId ){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse());
    }


}
