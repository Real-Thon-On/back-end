package com.fizz.fizz_server.security.jwt.controller;

import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import com.fizz.fizz_server.security.jwt.dto.request.UsernameLoginRequestDto;
import com.fizz.fizz_server.security.jwt.dto.response.TokenResponseDto;
import com.fizz.fizz_server.security.jwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * refresh token 을 포함한 전반적인 인증 로직 담당
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    // 자체 로그인을 위한 API
    @PostMapping("/login")
    public ResponseEntity<ResponseBody<TokenResponseDto>> usernameLogin(@RequestBody UsernameLoginRequestDto request) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(authService.usernameLogin(request)));
    }


    /** RefreshToken을 통한 AccessToken & RefreshToken 재발급 */
    @PostMapping("/refresh")
    public ResponseEntity<ResponseBody<TokenResponseDto>> reissueTokens(@RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(authService.reissueTokens(bearerToken)));
    }

    /** 단일 로그아웃 - 해당 RefreshToken만 삭제 */
    @DeleteMapping("/logout")
    public ResponseEntity<ResponseBody<Void>> logout(@RequestHeader("Authorization") String bearerToken) {
        authService.logout(bearerToken);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse());
    }

    /** 전체 로그아웃 - 해당 유저의 모든 기기에서 로그아웃 */
    @DeleteMapping("/logout/all")
    public ResponseEntity<ResponseBody<Void>> logoutAll(@RequestParam Long userId) {
        authService.logoutAll(userId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse());
    }


}
