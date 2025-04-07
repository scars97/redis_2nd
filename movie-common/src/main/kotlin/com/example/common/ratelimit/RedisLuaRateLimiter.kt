package com.example.common.ratelimit

import com.example.common.ratelimit.LimitRequestPerTime
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component("RedisLuaRateLimiter")
class RedisLuaRateLimiter(
    private val rateLimitScript: RedisScript<Boolean>,
    private val redisTemplate: RedisTemplate<String, Any>
): RateLimiter {

    override fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): Boolean {
        return redisTemplate.execute(
            rateLimitScript,
            listOf(key),
            limitRequestPerTime.limitCount,
            TimeUnit.SECONDS.convert(limitRequestPerTime.ttl, limitRequestPerTime.ttlTimeUnit)
        )
    }

}