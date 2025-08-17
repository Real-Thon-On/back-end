package com.realthon.on.psychoTest.dto.response;

import com.realthon.on.community.dto.response.CommunityResponseDto;
import com.realthon.on.community.entity.Comment;
import com.realthon.on.psychoTest.entity.PsychoQuestion;
import com.realthon.on.psychoTest.entity.PsychoResult;
import com.realthon.on.psychoTest.entity.PsychoTestType;
import com.realthon.on.psychoTest.entity.TargetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PsychoTestResponseDto {

    @Getter
    @Builder
    public static class TestTypeDto {
        private Long id;
        private String name;
        private String scale;
        private int questionCount;
        private String scoringMethod;
        private int scoreRangeMin;
        private int scoreRangeMax;
        private String resultMapping;
    }

    public static PsychoTestResponseDto.TestTypeDto fromTestTypeEntity(PsychoTestType testType) {
        return PsychoTestResponseDto.TestTypeDto.builder()
                .id(testType.getId())
                .name(testType.getName())
                .scale(testType.getScale())
                .questionCount(testType.getQuestionCount())
                .scoringMethod(testType.getScoringMethod())
                .scoreRangeMin(testType.getScoreRangeMin())
                .scoreRangeMax(testType.getScoreRangeMax())
                .resultMapping(testType.getResultMapping())
                .build();
    }

    @Getter
    @Builder
    @Setter //choiceCount set 까먹지 말기
    public static class QuestionDto {
        private Long id;
        private String questionText;
        private int choiceCount;
        private List<ChoiceDto> choices;
    }

    @Getter
    @Builder
    public static class ChoiceDto {
        private Long id;
        private String text;
    }

    public static PsychoTestResponseDto.QuestionDto fromQuestionEntity(PsychoQuestion question) {
        List<PsychoTestResponseDto.ChoiceDto> choices = question.getChoices().stream()
                .map(c -> PsychoTestResponseDto.ChoiceDto.builder()
                        .id(c.getId())
                        .text(c.getText())
                        .build())
                .collect(Collectors.toList());

        return QuestionDto.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .choices(choices)
                .choiceCount(choices.size())
                .build();
    }

    @Getter
    @Builder
    public static class PsychoResultDto {
        private Long id;
        private Long userId;
        private Long testTypeId;
        private int totalScore;
        private String resultText;
    }

    public static PsychoTestResponseDto.PsychoResultDto fromResultEntity(PsychoResult result) {
        return PsychoResultDto.builder()
                .id(result.getId())
                .userId(result.getUserId())
                .testTypeId(result.getTestType().getId())
                .totalScore(result.getTotalScore())
                .resultText(result.getResultText())
                .build();
    }




}
