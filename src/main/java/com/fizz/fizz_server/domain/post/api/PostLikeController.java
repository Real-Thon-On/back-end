package com.fizz.fizz_server.domain.post.api;

import com.fizz.fizz_server.domain.post.dto.response.IsLikedResponse;
import com.fizz.fizz_server.domain.post.service.PostLikeService;
import com.fizz.fizz_server.domain.user.domain.CustomUserPrincipal;
import com.fizz.fizz_server.global.base.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.fizz.fizz_server.global.base.response.ResponseUtil.createSuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<ResponseBody<Void>> addLike(@PathVariable Long postId,
                                                      @AuthenticationPrincipal CustomUserPrincipal user) {
        postLikeService.addLike(postId, user.getUserId());
        return ResponseEntity.ok(createSuccessResponse());
    }

    @DeleteMapping
    public ResponseEntity<ResponseBody<Void>> removeLike(@PathVariable Long postId,
                                                         @AuthenticationPrincipal CustomUserPrincipal user) {
        postLikeService.removeLike(postId, user.getUserId());
        return ResponseEntity.ok(createSuccessResponse());
    }

    @GetMapping("/check")
    public ResponseEntity<ResponseBody<IsLikedResponse>> isLiked(@PathVariable Long postId,
                                                                 @AuthenticationPrincipal CustomUserPrincipal user) {
        IsLikedResponse response = postLikeService.isLiked(postId, user.getUserId());
        return ResponseEntity.ok(createSuccessResponse(response));
    }
}
