package com.example.common.ratelimit

interface RateLimiter {

    fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): Boolean

}