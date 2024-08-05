package com.fizz.fizz_server.domain.recommend.service;

import com.fizz.fizz_server.domain.challenge.repository.ParticipantRepository;
import com.fizz.fizz_server.domain.post.dto.response.PostInfo;
import com.fizz.fizz_server.domain.post.repository.PostLikeRepository;
import com.fizz.fizz_server.domain.post.repository.PostRepository;
import com.fizz.fizz_server.domain.recommend.dto.PostRecommendationInput;
import com.fizz.fizz_server.domain.recommend.recommend.PostRecommendation;
import com.fizz.fizz_server.domain.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class RecommendService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final FollowRepository followRepository;
    private final ParticipantRepository participantRepository;
    private final PostRecommendation postRecommendation;

    // TODO. 상수로 쓴 것들 여기에 선언
    private final int MAX_USER_LIKE_POSTS = 100;
    private final int MIN_POST_LIKES = 5;
    private final int MAX_RECOMMENDATIONS = 100;

    // 추천 알고리즘이 동작하는 동안 사용자가 볼 수 있게 반환해주는 post
    public Page<PostInfo> preRecommendPost(Long userId, Pageable pageable) {
        // TODO. 코드 작성
        return null;
    }

    // 실제 추천 알고리즘
    public List<PostInfo> recommendPost(Long userId) {
        // 사용자가 좋아요를 누른 최근 100개의 postId 가져오기
        Pageable pageable = PageRequest.of(0, MAX_USER_LIKE_POSTS, Sort.by(Sort.Order.desc("createdAt")));
        Set<Long> likePosts = new HashSet<>(postLikeRepository.findByUserId(userId, pageable).getContent());
        // TODO. 좋아요한 포스트가 적거나 없을 때의 처리

        // 일정한 최소 개수 이상의 Post 좋아요 있는 사용자만 추리기
        // 레파지토리단에서 이 작업 한번에 해주거나 등등 어떤 방법이 제일 성능에 좋을지 모르겠습니다...
        List<Long> users = postLikeRepository.findUsersWithMinLikes(MIN_POST_LIKES);
        Map<Long, Set<Long>> userItems = new HashMap<>();
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

        // 사용자가 팔로우하는 사람의 게시물 조회
        List<Long> followerPosts = followRepository.findFollowingPost(userId);

        // TODO. 사용자가 참여하고 있는 챌린지 게시물 가져오기


        // TODO. 사용자의 관심 카테고리의 게시물 가져오기


        // TODO. 추천 결과들 다 합치며 중복 제거하고 변환하여 return
        // 일단 테스트용으로 CF 결과만 테스트
        List<PostInfo> recommend = postRepository.findAllById(CFRecommendationPosts).stream()
                .map(PostInfo::from)
                .toList();
        return recommend;
    }

}
