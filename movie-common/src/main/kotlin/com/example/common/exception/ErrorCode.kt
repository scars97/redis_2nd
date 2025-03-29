package com.example.common.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus
) {
    // 좌석
    ALREADY_RESERVED(HttpStatus.CONFLICT),

    // 예약
    EXCEED_LIMIT(HttpStatus.BAD_REQUEST),
    NOT_SAME_ROW(HttpStatus.BAD_REQUEST),
    NOT_CONTINUOUS_SEAT(HttpStatus.BAD_REQUEST),

    // 회원
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),

    // 상영일정
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND)
}