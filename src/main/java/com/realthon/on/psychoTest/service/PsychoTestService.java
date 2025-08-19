package com.realthon.on.psychoTest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realthon.on.community.dto.response.CommunityResponseDto;
import com.realthon.on.global.base.response.exception.BusinessException;
import com.realthon.on.global.base.response.exception.ExceptionType;
import com.realthon.on.psychoTest.dto.request.PsychoTestReqeustDto;
import com.realthon.on.psychoTest.dto.response.PsychoTestResponseDto;
import com.realthon.on.psychoTest.entity.PsychoChoice;
import com.realthon.on.psychoTest.entity.PsychoResult;
import com.realthon.on.psychoTest.entity.PsychoTestType;
import com.realthon.on.psychoTest.entity.TargetType;
import com.realthon.on.psychoTest.repository.PsychoChoiceRepository;
import com.realthon.on.psychoTest.repository.PsychoQuestionRepository;
import com.realthon.on.psychoTest.repository.PsychoResultRepository;
import com.realthon.on.psychoTest.repository.PsychoTestTypeRepository;
import com.realthon.on.user.entity.User;
import com.realthon.on.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PsychoTestService {

    private final PsychoTestTypeRepository testTypeRepository;
    private final PsychoQuestionRepository questionRepository;
    private final PsychoChoiceRepository choiceRepository;
    private final PsychoResultRepository resultRepository;
    private final UserRepository userRepository;

    public List<PsychoTestResponseDto.TestTypeDto> getTests(TargetType type) {

        return testTypeRepository.findByTargetTypesIn(Set.of(type)).stream()
                .map(PsychoTestResponseDto::fromTestTypeEntity)
                .collect(Collectors.toList());
    }

    public List<PsychoTestResponseDto.QuestionDto> getTestDetail(Long testId) {
        return questionRepository.findByTestTypeId(testId).stream()
                .map(PsychoTestResponseDto::fromQuestionEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public PsychoTestResponseDto.PsychoResultDto submitTest(Long testId, Long userId, PsychoTestReqeustDto.PsychSubmitDto submit) {

        PsychoTestType testType = testTypeRepository.findById(testId)
                .orElseThrow(() -> new BusinessException(ExceptionType.TEST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));

        // 선택지 맵핑
        Map<Long, PsychoChoice> choiceMap = choiceRepository.findByQuestionTestTypeId(testId).stream()
                .collect(Collectors.toMap(PsychoChoice::getId, c -> c));


        // 총 점수 계산
        int totalScore = submit.getAnswers().stream()
                .mapToInt(a -> choiceMap.get(a.getChoiceId()).getScore())
                .sum();

        // JSON 문자열 → Map
        //readValue(변환할 JSON 문자열, 어떤 타입으로 변환할지)
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> resultMap;
        try {
            resultMap = objectMapper.readValue(
                    testType.getResultMapping(),
                    new TypeReference<Map<String, Map<String, String>>>() {}
            );
        } catch (JsonProcessingException e) {
            log.error("점수 결과 맵핑 에러", e);
            throw new BusinessException(ExceptionType.RESULT_SAVE_FAILED);
        }

        // 결과 도출
        String resultState = null; // 등급 (정상/위험 등)
        String resultMessage = null;    // 상세 메시지
        for (String range : resultMap.keySet()) {
            String[] bounds = range.split("-");
            int min = Integer.parseInt(bounds[0]);
            int max = Integer.parseInt(bounds[1]);
            if (totalScore >= min && totalScore <= max) {
                resultState = resultMap.get(range).get("resultState");
                resultMessage = resultMap.get(range).get("resultMessage");
                break;
            }
        }

        // 결과 저장
        PsychoResult result = PsychoResult.builder()
                .user(user)
                .testType(testType)
                .totalScore(totalScore)
                .resultState(resultState)
                .resultMessage(resultMessage)
                .build();

        resultRepository.save(result);
        return PsychoTestResponseDto.fromResultEntity(result);
    }

    public List<PsychoTestResponseDto.PsychoResultDto> getMyResults(Long userId) {
        return resultRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(PsychoTestResponseDto::fromResultEntity)
                .collect(Collectors.toList());
    }

}

