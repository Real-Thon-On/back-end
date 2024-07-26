package com.fizz.fizz_server.domain.user.controller;

import com.fizz.fizz_server.domain.user.domain.CustomUserPrincipal;
import com.fizz.fizz_server.domain.user.dto.request.CheckProfileIdRequest;
import com.fizz.fizz_server.domain.user.dto.request.UserInfoUpdateRequest;
import com.fizz.fizz_server.domain.user.dto.response.CheckProfileIdResponse;
import com.fizz.fizz_server.domain.user.dto.response.UserInfoResponse;
import com.fizz.fizz_server.domain.user.service.UserService;
import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.domain.user.dto.request.ProfileIdAndEmailSetRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.fizz.fizz_server.global.base.response.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login-info")
    public ResponseEntity<ResponseBody<Void>> newUser(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                                      @Valid @RequestBody ProfileIdAndEmailSetRequest request) {
        userService.setProfileIdAndEmail(userPrincipal.getUserId(), request.getProfileId(), request.getEmail());
        return ResponseEntity.ok(createSuccessResponse());
    }

    @PatchMapping("/me")
    public ResponseEntity<ResponseBody<Void>> me(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                                 @Valid @RequestBody UserInfoUpdateRequest request) {
        userService.updateUserInfo(userPrincipal.getUserId(), request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @GetMapping("/check/profileId")
    public ResponseEntity<ResponseBody<CheckProfileIdResponse>> isProfileIdDuplicate(@Valid @RequestBody CheckProfileIdRequest request) {

        CheckProfileIdResponse response = userService.isProfileIdDuplicate(request.getProfileId());
        return ResponseEntity.ok(createSuccessResponse(response));
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ResponseBody<UserInfoResponse>> getProfileInfo(@PathVariable String profileId) {
        UserInfoResponse response = userService.getProfileInfo(profileId);
        return ResponseEntity.ok(createSuccessResponse(response));
    }

    @PostMapping("/following/{userId}")
    public ResponseEntity<ResponseBody<Void>> followUser(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                                         @PathVariable Long userId) {
        userService.followUser(userPrincipal.getUserId(), userId);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @DeleteMapping("/following/{userId}")
    public ResponseEntity<ResponseBody<Void>> unfollowUser(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                                           @PathVariable Long userId) {
        userService.unfollowUser(userPrincipal.getUserId(), userId);
        return ResponseEntity.ok(createSuccessResponse());
    }

}
