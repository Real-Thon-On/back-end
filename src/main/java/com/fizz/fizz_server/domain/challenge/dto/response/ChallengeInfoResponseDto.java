package com.fizz.fizz_server.domain.challenge.dto.response;

import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import lombok.*;



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
    private String profileId;


    public static ChallengeInfoResponseDto toDTO(Challenge challenge, Integer participantCounts){
        ChallengeInfoResponseDto dto =  new ChallengeInfoResponseDto();
        dto.challengeId =challenge.getId();
        dto.categoryId =challenge.getCategory().getCategoryId();
        dto.userId=challenge.getCreator().getId();
        dto.title=challenge.getTitle();
        dto.description = challenge.getDescription();
        dto.isActive =challenge.isActive();
        dto.participantCounts = participantCounts;
        dto.profileId=challenge.getCreator().getProfileId();
        return dto;
    }


}
