package com.example.common.ratelimit

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LimitRequestPerTime(
    // 호출 제한 key
    val key: String,

    // 호출 제한 시간
    val ttl: Long,

    // 호출 제한 시간 단위
    val ttlTimeUnit: TimeUnit,

    // 분당 호출제한 카운트
    val limitCount: Long
)
