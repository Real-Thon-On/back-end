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
    USER_NOT_FOUND(NOT_FOUND, "U001", "존재하지 않는 사용자"),
    DUPLICATED_NICKNAME(CONFLICT, "U002", "중복 닉네임"),
    DUPLICATED_EMAIL(CONFLICT, "U003", "중복 이메일"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "U004", "권한이 없음"),
    UN_AUTHENTICATION(UNAUTHORIZED, "U005", "인증이 필요함"),

    // challenge
    NON_EXISTENT_CHALLENGE_ERROR(BAD_REQUEST,"CH001","존재하지 않는 챌린지"),


    // category
    NON_EXISTENT_CATEGORY_ERROR(BAD_REQUEST,"CA002","존재하지 않는 카테고리"),

    // post
    POST_NOT_FOUND(NOT_FOUND, "P001", "존재하지 않는 게시글"),
    POST_USER_NOT_MATCHED(BAD_REQUEST, "P002", "해당 게시글의 사용자가 아님"),
    FILE_NOT_FOUND(NOT_FOUND, "P003", "파일이 존재하지 않습니다."),
    ALREADY_LIKED(BAD_REQUEST, "P004", "이미 좋아요 누른 게시글입니다."),
    NOT_LIKED(BAD_REQUEST, "P005", "좋아요를 누르지 않은 게시글입니다."),

    // comment
    NON_EXISTENT_COMMENT_ERROR(BAD_REQUEST,"C007","존재하지 않는 댓글")

    ;


    private final HttpStatus status;
    private final String code;
    private final String message;

}
