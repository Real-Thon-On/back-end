package com.realthon.on.global.base.response.exception;

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
    DUPLICATE_VALUE_ERROR(NOT_ACCEPTABLE , "C005","값이 중복됨"),

    // auth
    INVALID_REFRESH_TOKEN(NOT_ACCEPTABLE , "A001","유효하지 않은 리프레시 토큰"),
    REFRESH_TOKEN_EXPIRED(UNAUTHORIZED,"A002","리프레시 토큰 만료"),
    PASSWORD_NOT_MATCHED(NOT_ACCEPTABLE , "A003","비밀번호 불일치"),

    // oauth2
    INVALID_PROVIDER_TYPE_ERROR(NOT_ACCEPTABLE , "O001","지원하지 않는 provider"),

    // user
    USER_NOT_FOUND(NOT_FOUND, "U001", "존재하지 않는 사용자"),
    DUPLICATED_USER_ID(CONFLICT, "U002", "중복 아이디(PK)"),
    DUPLICATED_USERNAME(CONFLICT, "U003", "중복 아이디(username)"),
    ALREADY_REGISTERED_USER(NOT_ACCEPTABLE , "U006","이미 최종 회원 가입된 사용자"),
    NOT_REGISTERED_USER(FORBIDDEN , "U007","최종 회원 가입 되지 않은 사용자"),
    UNAUTHORIZED_USER(UNAUTHORIZED, "U005","로그인 되지 않은 사용자"),
    ACCESS_DENIED(FORBIDDEN, "U008", "권한이 없습니다."),



    //store
    STORE_NOT_FOUND(NOT_FOUND, "S001", "존재하지 않는 가게"),

    //diary
    DIARY_NOT_FOUND(NOT_FOUND, "D001", "존재하지 않는 일기"),
    RESULT_NOT_FOUNT(NOT_FOUND, "D001", "최근 분석 결과가 없습니다."),

    //board
    BOARD_NOT_FOUND(NOT_FOUND, "B001", "존재하지 않는 게시글"),
    COMMENT_NOT_FOUND(NOT_FOUND, "B002", "존재하지 않는 댓글"),
    LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "B003", "이미 좋아요가 존재합니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "B004", "좋아요가 존재하지 않습니다."),

    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "B005", "이미지 업로드 실패"),

    //psychoTest

    //test
    TEST_NOT_FOUND(NOT_FOUND, "T001", "존재하지 않는 게시글"),
    RESULT_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "T002", "심리검사 결과 저장 실패");

    private final HttpStatus status;
    private final String code;
    private final String message;}
