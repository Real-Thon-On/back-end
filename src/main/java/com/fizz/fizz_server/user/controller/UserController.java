package com.fizz.fizz_server.user.controller;



import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import com.fizz.fizz_server.security.oauth2.principal.PrincipalDetails;
import com.fizz.fizz_server.user.dto.request.SignUpRequestDto;
import com.fizz.fizz_server.user.dto.request.UserUpdateRequestDto;
import com.fizz.fizz_server.user.dto.response.SignUpResponseDto;
import com.fizz.fizz_server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ResponseBody<SignUpResponseDto>> signup(@RequestBody SignUpRequestDto requestDto) {
        SignUpResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    /**
     * OAuth2 혹은 자체 회원 가입 까지 완료한 NOT_REGISTERED 권한의 사용자들에 대해
     * 추가 작업을 완료하고 USER 권한으로 승격하여
     * 최종 회원 가입 진행
     * @param requestDto
     * @return
     */
    @PostMapping("/complete-sign-up")
    public ResponseEntity<ResponseBody<Void>> completeSignup(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody UserUpdateRequestDto requestDto
    ) {
        userService.completeSignup(principal.getUser().getId(), requestDto);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse());
    }




}

