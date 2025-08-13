package com.realthon.on.emotionDiary.service;

import com.realthon.on.emotionDiary.dto.request.EmotionDiaryRequestDto;
import com.realthon.on.emotionDiary.dto.response.EmotionDiaryResponseDto;
import com.realthon.on.emotionDiary.entity.EmotionDiary;
import com.realthon.on.emotionDiary.repository.EmotionDiaryRepository;
import com.realthon.on.global.base.response.exception.BusinessException;
import com.realthon.on.global.base.response.exception.ExceptionType;
import com.realthon.on.user.entity.User;
import com.realthon.on.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EmotrionDiaryService {
    private final EmotionDiaryRepository emotionDiaryRepository;
    private final UserRepository userRepository;

    //감정일기 생성
    @Transactional
    public EmotionDiaryResponseDto createDiary(EmotionDiaryRequestDto.AddEmotionDiaryRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));
        EmotionDiary diary = EmotionDiary.builder()
                .user(user)
                .weather(request.getWeather())
                .date(request.getDate())
                .hashtags(request.getHashtags())
                .content(request.getContent())
                .build();
        EmotionDiary savedDiary = emotionDiaryRepository.save(diary);
        return EmotionDiaryResponseDto.fromEntity(savedDiary);
    }

    //특정 감정일기 조회
    public EmotionDiaryResponseDto getDiaryById(Long id) {
        EmotionDiary diary = emotionDiaryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionType.DIARY_NOT_FOUND));
        return EmotionDiaryResponseDto.fromEntity(diary);
    }

    public List<EmotionDiaryResponseDto> getAllByUserId(Long userId) {
        return emotionDiaryRepository.findAllByUser_IdOrderByCreatedAtDesc(userId)
                .stream()
                .map(EmotionDiaryResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    //감정일기 수정
    @Transactional
    public EmotionDiaryResponseDto updateDiary(Long id, EmotionDiaryRequestDto.UpdateEmotionDiaryRequestDto request,Long authenticatedUserId) {
        EmotionDiary diary = emotionDiaryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionType.DIARY_NOT_FOUND));

        if (!diary.getUser().getId().equals(authenticatedUserId)) {
            throw new BusinessException(ExceptionType.ACCESS_DENIED);
        }

        diary.update(request.getDate(), request.getWeather(),request.getHashtags(), request.getContent()); // update 메서드에 감정 필드 추가
        return EmotionDiaryResponseDto.fromEntity(diary);
    }
    //감정일기 삭제 (Delete)
    @Transactional // 쓰기 작업이므로 트랜잭션 설정
    public void deleteDiary(Long id) {
        emotionDiaryRepository.deleteById(id);
    }
}
