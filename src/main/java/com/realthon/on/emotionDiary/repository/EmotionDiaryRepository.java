package com.realthon.on.emotionDiary.repository;

import com.realthon.on.emotionDiary.entity.EmotionDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmotionDiaryRepository extends JpaRepository<EmotionDiary, Long> {
    List<EmotionDiary> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

}