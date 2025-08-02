package com.fizz.fizz_server.security.jwt.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TokenStatus {
    AUTHENTICATED,
    EXPIRED,
    INVALID
}
