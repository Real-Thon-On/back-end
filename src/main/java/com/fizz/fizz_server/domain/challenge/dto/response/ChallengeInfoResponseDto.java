package com.fizz.fizz_server.domain.challenge.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import lombok.*;

import java.time.LocalDateTime;


@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChallengeInfoResponseDto {
    private Long challengeId;
    private Long categoryId;
    private Long userId;
    private String title;
    private String description;
    private Boolean isActive;
    private Integer participantCounts;


    public static ChallengeInfoResponseDto toDTO(Challenge challenge, Integer participantCounts){
        ChallengeInfoResponseDto dto =  new ChallengeInfoResponseDto();
        dto.challengeId =challenge.getId();
        dto.categoryId =challenge.getCategory().getCategoryId();
        dto.userId=challenge.getCreator().getId();
        dto.title=challenge.getTitle();
        dto.description = challenge.getDescription();
        dto.isActive =challenge.isActive();
        dto.participantCounts = participantCounts;
        return dto;
    }


}
