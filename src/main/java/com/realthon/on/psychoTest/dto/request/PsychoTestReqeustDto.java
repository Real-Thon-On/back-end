package com.realthon.on.psychoTest.dto.request;

import com.realthon.on.psychoTest.dto.response.PsychoTestResponseDto;
import lombok.*;

import java.util.List;

public class PsychoTestReqeustDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PsychSubmitDto {
        private List<PsychoTestReqeustDto.UserAnswerDto> answers;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserAnswerDto {
        private Long questionId;
        private Long choiceId;
    }


}
