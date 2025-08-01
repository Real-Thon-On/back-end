package com.fizz.fizz_server.user.controller;



import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import com.fizz.fizz_server.user.dto.request.SignUpRequestDto;
import com.fizz.fizz_server.user.dto.response.UserResponseDto;
import com.fizz.fizz_server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 자체 회원 가입 API
     * @param requestDto
     * @return
     */
    @PostMapping("/sign-up")
    public ResponseEntity<ResponseBody<UserResponseDto>> signup(@RequestBody SignUpRequestDto requestDto) {
        UserResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }



}

