package com.fizz.fizz_server.domain.recommend.recommend;

import com.fizz.fizz_server.domain.recommend.dto.PostRecommendationInput;

import java.util.List;

public interface PostRecommendation {
    List<Long> recommend(PostRecommendationInput input);
}
