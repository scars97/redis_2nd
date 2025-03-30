package com.example.common.lock

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(
    // 락 이름
    val key: String,

    // 락 시간 단위
    val timeUnit: TimeUnit = TimeUnit.SECONDS,

    // 락 대기 시간 - 락을 얻기 위해 최대 기다리는 시간
    val waitTime: Long = 3L,

    // 락 임대 시간 - 락을 얻은 후 자동 해제되는 시간
    val leaseTime: Long = 2L
)

