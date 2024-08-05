package com.fizz.fizz_server.domain.recommend.dto;

import com.fizz.fizz_server.domain.post.domain.Post;
import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
public record PostRecommendationInput (
        Long userId,
        Set<Long> likePost,
        Map<Long, Set<Long>> userItems,
        int maxRecommendations
) {}
