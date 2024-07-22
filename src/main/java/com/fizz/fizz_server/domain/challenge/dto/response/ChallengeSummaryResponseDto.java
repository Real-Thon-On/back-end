package com.fizz.fizz_server.domain.challenge.dto.response;

import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import lombok.*;

@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChallengeSummaryResponseDto {

        private Long challengeId;
        private Long categoryId;
        private String title;
        private Integer participantCounts;


        public static ChallengeSummaryResponseDto toDTO(Challenge challenge, Integer participantCounts){
                ChallengeSummaryResponseDto dto = new ChallengeSummaryResponseDto();
                dto.challengeId =challenge.getId();
                dto.categoryId =challenge.getCategory().getCategoryId();
                dto.title=challenge.getTitle();
                dto.participantCounts = participantCounts;
                return dto;
        }


}
