package com.fizz.fizz_server.domain.recommend.recommend;


import com.fizz.fizz_server.domain.recommend.dto.PostRecommendationInput;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PostRecommendationByItemBasedCF implements PostRecommendation {
    // 사용자-아이템 데이터
    private Map<Long, Set<Long>> userData;
    // 아이템 간의 유사도 저장
    private Map<Long, Map<Long, Double>> itemSimilarity;

    @Override
    public List<Long> recommend(PostRecommendationInput input) {
        // 초기화 - 일정 시간마다 업데이트 해주는 로직이 성능에 좋으나, 시현 특성 상 빠르게 결과를 내기 위해 함수 호출마다 초기화
        this.init(input.userItems());

        // 추천해줄 유저의 아이템
        Set<Long> likedItems = input.likePost();
        // 일정 시간마다 업데이트 해주는 로직일 경우 다음과 같이 초기화
        // Set<Long> likedItems = userData.getOrDefault(input.userId(), new HashSet<>());

        Map<Long, Double> scores = new HashMap<>();

        // 유사도를 기반으로 추천 점수 계산
        for (Long item : likedItems) {
            for (Map.Entry<Long, Double> entry : itemSimilarity.getOrDefault(item, new HashMap<>()).entrySet()) {
                Long otherItem = entry.getKey();
                double similarity = entry.getValue();
                if (!likedItems.contains(otherItem)) {
                    scores.put(otherItem, scores.getOrDefault(otherItem, 0.0) + similarity);
                }
            }
        }

        // 점수가 높은 순으로 정렬하여 상위 k개 아이템 반환
        List<Long> recommendations = new ArrayList<>(scores.keySet());
        recommendations.sort((a, b) -> Double.compare(scores.get(b), scores.get(a)));

        return recommendations.subList(0, Math.min(input.maxRecommendations(), recommendations.size()));
    }

    public void init(Map<Long, Set<Long>> userItems) {
        userData = userItems;
        itemSimilarity = new HashMap<>();

        calculateItemSimilarities();
    }

    // 코사인 유사도를 계산하는 메서드
    private void calculateItemSimilarities() {
        Map<Long, Integer> itemCount = new HashMap<>();
        Map<Long, Map<Long, Integer>> coCount = new HashMap<>();

        // 아이템 간 공동 출현 횟수 계산
        for (Set<Long> items : userData.values()) {
            for (Long item : items) {
                itemCount.put(item, itemCount.getOrDefault(item, 0) + 1);
                coCount.putIfAbsent(item, new HashMap<>());

                for (Long otherItem : items) {
                    if (item.equals(otherItem)) continue;
                    coCount.get(item).put(otherItem, coCount.get(item).getOrDefault(otherItem, 0) + 1);
                }
            }
        }

        // 코사인 유사도 계산
        for (Long item : coCount.keySet()) {
            itemSimilarity.putIfAbsent(item, new HashMap<>());
            for (Long otherItem : coCount.get(item).keySet()) {
                // 좋아요 여부를 백터로 표현할 때, 내적 -> 두 아이템의 동시 출현 횟수, norm(Euclidean) -> 각 아이템의 출현 횟수의 제곱근의 곱
                double similarity = coCount.get(item).get(otherItem) / Math.sqrt(itemCount.get(item) * itemCount.get(otherItem));
                itemSimilarity.get(item).put(otherItem, similarity);
            }
        }
    }

}
