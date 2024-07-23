package com.fizz.fizz_server.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NicknameAndEmailSetRequest {
    private String nickname;
    private String email;
}
