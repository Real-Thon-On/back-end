package com.fizz.fizz_server.domain.post.service;

import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import com.fizz.fizz_server.domain.challenge.repository.ChallengeRepository;
import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.domain.post.dto.request.PostRequest;
import com.fizz.fizz_server.domain.post.dto.response.PostInfo;
import com.fizz.fizz_server.domain.post.repository.PostRepository;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.fizz.fizz_server.global.base.response.exception.ExceptionType.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional
    public void upload(Long challengeId, PostRequest request, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(NON_EXISTENT_CHALLENGE_ERROR));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        List<String> fileUrls = new ArrayList<>();
        if (request.images() != null && !request.images().isEmpty()) {
            fileUrls.addAll(request.images());
        } else if (request.video() != null && !request.video().isEmpty()) {
            fileUrls.add(request.video());
        }

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .challenge(challenge)
                .user(user)
                .fileUrls(fileUrls)
                .build();

        postRepository.save(post);
    }

    public PostInfo getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ExceptionType.POST_NOT_FOUND));
        return PostInfo.from(post);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ExceptionType.POST_NOT_FOUND));

        if(!post.getUser().getId().equals(userId)) {
            throw new BusinessException(ExceptionType.POST_USER_NOT_MATCHED);
        }

        postRepository.delete(post);
    }

    public Page<PostInfo> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostInfo::from);
    }

    public Page<PostInfo> getPostsByChallenge(Long challengeId, Pageable pageable) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(NON_EXISTENT_CHALLENGE_ERROR));

        return postRepository.findByChallenge(challenge, pageable)
                .map(PostInfo::from);
    }

    public Page<PostInfo> getPostsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        return postRepository.findByUser(user, pageable)
                .map(PostInfo::from);
    }

    public Page<PostInfo> searchPosts(String keyword, Pageable pageable) {
        return postRepository.searchByKeyword(keyword, pageable)
                .map(PostInfo::from);
    }


}
