package com.fizz.fizz_server.domain.challenge.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChallengeInfoRequestDto {

        @NotBlank(message = "title은 빈값일 수 없습니다.")
        private String title;


}
