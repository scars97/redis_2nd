package com.example.common.ratelimit

import io.github.bucket4j.Bucket
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Component("Bucket4jRateLimiter")
class Bucket4jRateLimiter: RateLimiter {

    private val cache = ConcurrentHashMap<String, Bucket>()

    override fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): Boolean {
        val bucket = resolveBucket(key, limitRequestPerTime)

        return bucket.tryConsume(1)
    }

    private fun resolveBucket(key: String, limitRequestPerTime: LimitRequestPerTime): Bucket {
        return cache.computeIfAbsent(key) {
            Bucket.builder()
                .addLimit { limit ->
                    limit.capacity(limitRequestPerTime.limitCount)
                        .refillGreedy(
                            limitRequestPerTime.limitCount,
                            toDuration(limitRequestPerTime.ttl, limitRequestPerTime.ttlTimeUnit)
                        )
                }
                .build()
        }
    }

    private fun toDuration(amount: Long, unit: TimeUnit): Duration {
        val millis = unit.toMillis(amount)
        return Duration.ofMillis(millis)
    }

}