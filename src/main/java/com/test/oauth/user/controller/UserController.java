package com.test.oauth.user.controller;

import com.test.oauth.global.base.response.ResponseBody;
import com.test.oauth.global.base.response.ResponseUtil;
import com.test.oauth.security.oauth2.principal.PrincipalDetails;
import com.test.oauth.user.dto.request.SignUpRequestDto;
import com.test.oauth.user.dto.request.UserUpdateRequestDto;
import com.test.oauth.user.dto.response.SignUpResponseDto;
import com.test.oauth.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "유저 및 회원가입 관련 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "자체 회원가입", description = "자체 로그인 기반의 기본 회원가입을 수행합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<ResponseBody<SignUpResponseDto>> signup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "회원가입 요청 정보", required = true,
                    content = @Content(schema = @Schema(hidden = true))
            )
            @RequestBody SignUpRequestDto requestDto
    ) {
        SignUpResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @Operation(
            summary = "최종 회원가입 완료",
            description = "OAuth2 로그인 또는 자체 로그인 후 추가 정보 입력을 통해 최종 회원가입을 완료합니다.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @PostMapping("/complete-sign-up")
    public ResponseEntity<ResponseBody<Void>> completeSignup(
            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails principal,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "닉네임, 이메일 등의 추가 정보",
                    required = true,
                    content = @Content(schema = @Schema(hidden = true))
            )
            @RequestBody UserUpdateRequestDto requestDto
    ) {
        userService.completeSignup(principal.getUser().getId(), requestDto);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse());
    }

}
