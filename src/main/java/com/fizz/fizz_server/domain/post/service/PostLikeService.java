package com.fizz.fizz_server.domain.post.service;

import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.domain.post.domain.PostLike;
import com.fizz.fizz_server.domain.post.dto.response.IsLikedResponse;
import com.fizz.fizz_server.domain.post.repository.PostLikeRepository;
import com.fizz.fizz_server.domain.post.repository.PostRepository;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.fizz.fizz_server.global.base.response.exception.ExceptionType.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addLike(Long postId, Long userId) {
        Post post = getPostById(postId);
        User user = getUserById(userId);

        checkIfLiked(post, user);

        PostLike postLike = PostLike.builder()
                .post(post)
                .user(user)
                .build();
        postLikeRepository.save(postLike);
    }

    @Transactional
    public void removeLike(Long postId, Long userId) {
        Post post = getPostById(postId);
        User user = getUserById(userId);

        PostLike postLike = postLikeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new BusinessException(NOT_LIKED));

        postLikeRepository.delete(postLike);
    }

    public IsLikedResponse isLiked(Long postId, Long userId) {
        Post post = getPostById(postId);
        User user = getUserById(userId);

        return new IsLikedResponse(postLikeRepository.existsByPostAndUser(post, user));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(POST_NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }

    private void checkIfLiked(Post post, User user) {
        if (postLikeRepository.existsByPostAndUser(post, user)) {
            throw new BusinessException(ALREADY_LIKED);
        }
    }
}
