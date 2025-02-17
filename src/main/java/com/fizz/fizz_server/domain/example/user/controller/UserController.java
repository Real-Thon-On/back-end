package com.fizz.fizz_server.domain.example.user.controller;

import com.fizz.fizz_server.domain.example.user.domain.CustomUserPrincipal;
import com.fizz.fizz_server.domain.example.user.service.UserService;
import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.domain.example.user.dto.request.ProfileIdAndEmailSetRequest;
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



}
