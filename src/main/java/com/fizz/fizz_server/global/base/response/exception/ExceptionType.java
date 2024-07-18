package com.fizz.fizz_server.global.base.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ExceptionType {
    // common
    UNEXPECTED_SERVER_ERROR(INTERNAL_SERVER_ERROR, "C001", "예상치못한 서버에러 발생"),
    BINDING_ERROR(BAD_REQUEST, "C002", "바인딩시 에러 발생"),
    ESSENTIAL_FIELD_MISSING_ERROR(NO_CONTENT , "C003","필수적인 필드 부재"),
    INVALID_VALUE_ERROR(NOT_ACCEPTABLE , "C004","값이 유효하지 않음"),

    // user

    // challenge
    NON_EXISTENT_CHALLENGE_ERROR(BAD_REQUEST,"C006","존재하지 않는 챌린지"),


    // category
    NON_EXISTENT_CATEGORY_ERROR(BAD_REQUEST,"C005","존재하지 않는 카테고리")

    // post

    // comment

    ;


    private final HttpStatus status;
    private final String code;
    private final String message;

}
