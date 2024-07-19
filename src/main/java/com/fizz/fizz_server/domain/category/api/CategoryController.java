package com.fizz.fizz_server.domain.category.api;


import com.fizz.fizz_server.domain.category.dto.request.CategoryRecommendRequestDto;
import com.fizz.fizz_server.domain.category.dto.request.CategoryRequestDto;
import com.fizz.fizz_server.domain.category.service.CategoryService;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    // UserPrincipal 추후 추가되면 삭제 예정
    private final UserRepository userRepository;


    //카테고리 생성 건의
    @PostMapping("/user")
    public ResponseEntity<ResponseBody> createCategoryRecommend(@Valid @RequestBody CategoryRecommendRequestDto requestDto){// parameter 에 @AuthenticationPrincipal UserPrincipal user 추후 추가
        User tempUser = userRepository.findById(1L).get(); // UserPrincipal 추후 추가되면 삭제.

        categoryService.createCategoryRecommend( tempUser , requestDto);
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
