package org.fastcampus.applicationclient.member.exception

import org.springframework.http.HttpStatus

enum class MemberExceptionResult(val httpStatus: HttpStatus, val message: String) {
    HEADER_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "헤더 토큰 값이 비어 있습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다. 다시 시도해 주세요."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레쉬 토큰입니다. 다시 시도해 주세요."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "가입된 아이디를 찾을 수 없습니다."),
    DUPLICATE_MEMBER(HttpStatus.BAD_REQUEST, "이미 가입된 계정이 존재합니다."),
}
