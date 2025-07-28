package com.fizz.fizz_server.user.controller;



import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import com.fizz.fizz_server.security.util.CustomUserDetails;
import com.fizz.fizz_server.user.dto.request.UserRequestDto;
import com.fizz.fizz_server.user.dto.response.UserResponseDto;
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

    @PostMapping("/signup")
    public ResponseEntity<ResponseBody<UserResponseDto>> signup(@RequestBody UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseBody<String>> login(@RequestBody UserRequestDto requestDto) {
        String token = userService.login(requestDto);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(token));
    }

    @GetMapping("/profile")
    public ResponseEntity<ResponseBody<UserResponseDto>> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponseDto responseDto = userService.getProfile(userDetails.getId());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @PutMapping("/profile")
    public ResponseEntity<ResponseBody<UserResponseDto>> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserRequestDto request
    ) {
        UserResponseDto responseDto = userService.update(userDetails.getId(), request);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ResponseBody<Void>> delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.delete(userDetails.getId());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse());
    }
}

