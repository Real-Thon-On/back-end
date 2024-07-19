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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    public static ChallengeInfoResponseDto EntityToDTO(Challenge challenge){
        ChallengeInfoResponseDto dto =  new ChallengeInfoResponseDto();
        dto.challengeId =challenge.getId();
        dto.categoryId =challenge.getCategory().getCategoryId();
        dto.userId=challenge.getCreator().getId();
        dto.title=challenge.getTitle();
        dto.description = challenge.getDescription();
        dto.startDate=challenge.getStartDate();
        return dto;
    }


}
