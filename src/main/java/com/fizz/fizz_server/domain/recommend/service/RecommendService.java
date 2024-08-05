package com.fizz.fizz_server.domain.recommend.service;

import com.fizz.fizz_server.domain.challenge.repository.ParticipantRepository;
import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.domain.post.dto.response.PostInfo;
import com.fizz.fizz_server.domain.post.repository.PostLikeRepository;
import com.fizz.fizz_server.domain.post.repository.PostRepository;
import com.fizz.fizz_server.domain.recommend.dto.PostRecommendationInput;
import com.fizz.fizz_server.domain.recommend.recommend.PostRecommendation;
import com.fizz.fizz_server.domain.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class RecommendService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final FollowRepository followRepository;
    private final ParticipantRepository participantRepository;
    private final PostRecommendation postRecommendation;

    private final int MAX_USER_LIKE_POSTS = 100;
    private final int MIN_POST_LIKES = 5;
    private final int MAX_RECOMMENDATIONS = 100;

    @Transactional(readOnly = true)
    public List<PostInfo> recommendPost(Long userId) {
        // 사용자가 좋아요를 누른 최근 100개의 postId 가져오기
        Pageable pageable = PageRequest.of(0, MAX_USER_LIKE_POSTS, Sort.by(Sort.Order.desc("createdAt")));
        Set<Long> likePosts = new HashSet<>(postLikeRepository.findByUserId(userId, pageable).getContent());

        // 추천 결과들을 다 합치며 중복 제거
        Set<Long> recommendPosts = new HashSet<>();

        if (!likePosts.isEmpty()) {
            // 일정한 최소 개수 이상의 Post 좋아요 있는 사용자만 추리기
            List<Long> users = postLikeRepository.findUsersWithMinLikes(MIN_POST_LIKES);
            Map<Long, Set<Long>> userItems = new ConcurrentHashMap<>();
            for (Long user : users) {
                userItems.put(user, postLikeRepository.findPostsLikedByUsers(user));
            }

            List<Long> CFRecommendationPosts = postRecommendation.recommend(
                    PostRecommendationInput.builder()
                            .userId(userId)
                            .likePost(likePosts)
                            .userItems(userItems)
                            .maxRecommendations(MAX_RECOMMENDATIONS)
                            .build()
            );

            recommendPosts.addAll(CFRecommendationPosts);
        }

        // 사용자가 팔로우하는 사람의 게시물 조회
        List<Long> followingPosts = followRepository.findFollowingPost(userId);
        recommendPosts.addAll(followingPosts);

        // 사용자가 참여하고 있는 챌린지의 게시물 조회
        List<Long> challengePosts = participantRepository.findPostIdByUserChallenges(userId);
        recommendPosts.addAll(challengePosts);

        // 사용자가 작성한 게시물 제거
        List<Long> userPosts = postRepository.findPostIdByUserId(userId);
        userPosts.forEach(recommendPosts::remove);

        return postRepository.findAllById(recommendPosts).stream()
                .map(PostInfo::from)
                .toList();
    }

}
