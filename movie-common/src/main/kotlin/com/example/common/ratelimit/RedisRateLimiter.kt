package com.example.common.ratelimit

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component("RedisRateLimiter")
class RedisRateLimiter(
    private val redisTemplate: RedisTemplate<String, Any>
): RateLimiter {

    override fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): Boolean {
        val previousCount = (redisTemplate.opsForValue().get(key) as? Number)?.toLong()

        if (previousCount != null && previousCount > limitRequestPerTime.limitCount) {
            return false
        }

        if (previousCount == null) {
            redisTemplate.opsForValue().set(key, 0, limitRequestPerTime.ttl, limitRequestPerTime.ttlTimeUnit)
        }

        redisTemplate.opsForValue().increment(key)

        return true
    }

}