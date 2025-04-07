package com.example.common.exception

class RateLimitException(
    val methodName: String
): RuntimeException("요청 제한을 초과했습니다. 잠시 후 다시 시도해주세요.") {
}