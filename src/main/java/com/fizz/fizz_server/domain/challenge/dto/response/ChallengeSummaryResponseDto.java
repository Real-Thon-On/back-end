package com.fizz.fizz_server.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChallengeSummaryResponseDto {

        private Long challengeId;
        private Long categoryId;
        private String title;
        private Long participantCounts;


}
